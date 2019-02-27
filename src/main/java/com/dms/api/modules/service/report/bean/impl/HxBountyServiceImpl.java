package com.dms.api.modules.service.report.bean.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.dms.api.common.utils.Query;
import com.dms.api.modules.dao.report.bean.HxBountyDao;
import com.dms.api.modules.entity.basis.org.BmSysOrganization;
import com.dms.api.modules.entity.report.bean.*;
import com.dms.api.modules.entity.sys.user.SysUserEntity;
import com.dms.api.modules.service.basis.org.BmOrganizationService;
import com.dms.api.modules.service.report.bean.HxBountyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("hxBountyService")
public class HxBountyServiceImpl implements HxBountyService {

    @Autowired
    private HxBountyDao hxBountyDao;

    @Autowired
    private BmOrganizationService bmOrganizationService;

    /*
     * // K豆总额 int totalBounty();
     *
     * //可用K豆总额 int usableBounty();
     *
     * //已消费K豆总额 int consumeBounty();
     *
     * //已过期K豆总额 int pastBounty();
     */
    @Override
    public Map<String, Object> countBounty(SysUserEntity user) {

        Map<String, Object> result = new HashMap<>();
        BigDecimal totalBounty = new BigDecimal(0);
        BigDecimal usableBounty = new BigDecimal(0);
        BigDecimal consumeBounty = new BigDecimal(0);
        BigDecimal pastBounty = new BigDecimal(0);
        BigDecimal valalidBounty = new BigDecimal(0);
        if (user.isSuperAdmin() || user.getDealerId() != null) {
            Query query = new Query();
            //获取今天到15之内
            query.put("dayStart", dateConvertString(getNextDay(new Date(), 0)) + " 00:00:00");
            query.put("dayEnd", dateConvertString(getNextDay(new Date(), 15)) + " 23:59:59");
            query.put("dealerId", user.getDealerId());
            totalBounty = hxBountyDao.totalBounty(query);
            usableBounty = hxBountyDao.usableBounty(query);
            consumeBounty = hxBountyDao.consumeBounty(query);
            pastBounty = hxBountyDao.pastBounty(query);
            valalidBounty = hxBountyDao.valalidBounty(query);
        }

        result.put("totalBounty", totalBounty == null ? 0 : totalBounty);
        result.put("usableBounty", usableBounty == null ? 0 : usableBounty);
        result.put("consumeBounty", consumeBounty == null ? 0 : consumeBounty);
        result.put("pastBounty", pastBounty == null ? 0 : pastBounty);
        if(valalidBounty==null){
            valalidBounty=new BigDecimal(0);
        }
        if(valalidBounty.doubleValue()<0){
            valalidBounty=new BigDecimal(0);
        }
        result.put("LastTwoBounty", valalidBounty == null ? 0 : valalidBounty);
        return result;
    }

    /**
     * 服务商K豆统计优化
     */
    @Override
    public List<DealerCount> countDealerBounty(Query query) {
        List<DealerCount> dealerCounts = new ArrayList<>();
        SysUserEntity user = (SysUserEntity) query.get("user");
        if (user != null && (user.isSuperAdmin() || user.getDealerId() != null)) {
            query.put("dealerId", user.getDealerId());
            //查询服务商列表
            dealerCounts = hxBountyDao.dealerListFirst(query);
            if (dealerCounts.size() > 0) {
                query.put("dealerList", dealerCounts);
                //统计日结历史数据
                List<BountyDailySettlementEntity> historyList = hxBountyDao.countHistory(query);
                //账单表，用于统计总额、可用总额、已消费总额、15日内过期
                List<BBillEntity> bills = hxBountyDao.billsByDealers(query);
                dealerCounts.parallelStream()
                        .forEach(dealerCount -> {
                            dealerCount.init();
                            historyList.parallelStream()
                                    .filter(entity -> entity.getDealerId().equals(dealerCount.getDealerId() + ""))
                                    .findAny().ifPresent(entity -> {
                                dealerCount.setTotal(entity.getIncome());
                                dealerCount.setConsume(entity.getConsume());
                                dealerCount.setInvalid(entity.getInvalid());
                            });
                            bills.stream()
                                    .filter(bill -> bill.getDealerId().equals(dealerCount.getDealerId()))
                                    .forEach(bill -> {
                                        bill.setSurplusBounty(bill.getSurplusBounty() == null ? new BigDecimal(0) : bill.getSurplusBounty());
                                        bill.setValalidBounty(bill.getValalidBounty() == null ? new BigDecimal(0) : bill.getValalidBounty());
                                        if (bill.isValid()) {
                                            dealerCount.setUsable(dealerCount.getUsable().add(bill.getSurplusBounty()));
                                            dealerCount.setMorrow(dealerCount.getMorrow().add(bill.getValalidBounty()));
                                            if(dealerCount.getMorrow().doubleValue()<0){
                                                dealerCount.setMorrow(new BigDecimal(0));
                                            }
                                        }
                                    });

                        });
                String currentDay = cuurentDay();
                if ((query.get("endTime") + "").compareTo(currentDay) >= 0 || (query.get("startTime") + "").compareTo(currentDay) >= 0 || "".equals(query.get("endTime") + "")) {
                    //若查询时间包括当天则统计流水中当天的数据
                    List<DealerCount> incomeAndConsumeList = hxBountyDao.countCurrentByDealerId(query);
                    List<DealerCount> invalidList = hxBountyDao.countCurrentInvalidByDealerId(query);
                    dealerCounts.parallelStream()
                            .forEach(dealerCount -> {
                                incomeAndConsumeList.parallelStream()
                                        .filter(entity -> entity.getDealerId() == dealerCount.getDealerId())
                                        .findAny().ifPresent(entity -> {
                                    dealerCount.setTotal(dealerCount.getTotal().add(entity.getTotal()));
                                    dealerCount.setConsume(dealerCount.getConsume().add(entity.getConsume()));
                                });
                                invalidList.parallelStream()
                                        .filter(entity -> entity.getDealerId() == dealerCount.getDealerId())
                                        .findAny().ifPresent(entity -> {
                                    dealerCount.setInvalid(dealerCount.getInvalid().add(entity.getInvalid()));
                                });
                            });
                }

            }
        }
        return dealerCounts;
    }

    @Override
    public List<OrgCount> countOrgBounty(Query query) {
        List<OrgCount> orgCounts;
        SysUserEntity user = (SysUserEntity) query.get("user");
            if (null != user.getDealerId()) {
                query.put("dealerId", user.getDealerId());
            } else {
                query.put("orgIds", getOrgsByUser(user, query));
            }
            //查询机构列表
            orgCounts = hxBountyDao.orgList(query);
            if (orgCounts.size() > 0) {
                query.put("orgList", orgCounts);
                //统计日结历史数据
                List<BountyDailySettlementEntity> historyList = hxBountyDao.countHistory(query);
                //账单表，用于统计总额、可用总额、已消费总额、15日内过期
                List<BBillEntity> bills = hxBountyDao.billsByOrgs(query);
                orgCounts.parallelStream()
                        .forEach(orgCount -> {
                            orgCount.init();
                            historyList.parallelStream()
                                    .filter(entity -> entity.getOrgId().equals(orgCount.getOrgId() + ""))
                                    .findAny().ifPresent(entity -> {
                                orgCount.setTotal(entity.getIncome());
                                orgCount.setConsume(entity.getConsume());
                                orgCount.setInvalid(entity.getInvalid());
                            });
                            bills.stream()
                                    .filter(bill -> bill.getOrgId().equals(orgCount.getOrgId()))
                                    .forEach(bill -> {
                                        bill.setSurplusBounty(bill.getSurplusBounty() == null ? new BigDecimal(0) : bill.getSurplusBounty());
                                        bill.setValalidBounty(bill.getValalidBounty() == null ? new BigDecimal(0) : bill.getValalidBounty());
                                        if (bill.isValid()) {
                                            orgCount.setUsable(orgCount.getUsable().add(bill.getSurplusBounty()));
                                            orgCount.setMorrow(orgCount.getMorrow().add(bill.getValalidBounty()));
                                            if(orgCount.getMorrow().doubleValue()<0){
                                                orgCount.setMorrow(new BigDecimal(0));
                                            }
                                        }
                                    });
                        });
                String currentDay = cuurentDay();
                if ((query.get("endTime") + "").compareTo(currentDay) >= 0 || (query.get("startTime") + "").compareTo(currentDay) >= 0 || "".equals(query.get("endTime") + "")) {
                    //若查询时间包括当天则统计流水中当天的数据
                    List<OrgCount> incomeAndConsumeList = hxBountyDao.countCurrentByOrgId(query);
                    List<OrgCount> invalidList = hxBountyDao.countCurrentInvalidByOrgId(query);
                    orgCounts.parallelStream()
                            .forEach(orgCount -> {
                                incomeAndConsumeList.parallelStream()
                                        .filter(entity -> entity.getOrgId().longValue() == orgCount.getOrgId().longValue())
                                        .findAny().ifPresent(entity -> {
                                    orgCount.setTotal(orgCount.getTotal().add(entity.getTotal()));
                                    orgCount.setConsume(orgCount.getConsume().add(entity.getConsume()));
                                });
                                invalidList.parallelStream()
                                        .filter(entity -> entity.getOrgId().longValue() == orgCount.getOrgId().longValue())
                                        .findAny().ifPresent(entity -> {
                                    orgCount.setInvalid(orgCount.getInvalid().add(entity.getInvalid()));
                                });
                            });
                }
            }
        return orgCounts;
    }

    @Override
    public List<SingleCount> countSingleBounty(Query query) {
        SysUserEntity user = (SysUserEntity) query.get("user");
        if (!user.isSuperAdmin()) {
            if (null != user.getDealerId()) {
                query.put("dealerId", user.getDealerId());
            } else {
                query.put("orgIds", getOrgsByUser(user, query));
            }
        }
        List<SingleCount> singleCounts = hxBountyDao.singleInfo(query);
        if (singleCounts.size() > 0) {
            query.put("singles", singleCounts);
            //统计时间条件内的收入，支出，失效
            List<SingleCount> historyList = hxBountyDao.countSingleBounty(query);
            //统计总额，可用，已消费，即将过期
            List<BBillEntity> bills = hxBountyDao.billsBySingle(query);
            singleCounts.parallelStream().forEach(singleCount -> {
                singleCount.init();
                historyList.parallelStream()
                .filter(entity -> entity.getAccId() == singleCount.getAccId())
                .findAny().ifPresent(entity -> {
                    singleCount.setTotal(entity.getTotal());
                    singleCount.setConsume(entity.getConsume());
                    singleCount.setInvalid(entity.getInvalid());
                });
                bills.stream()
                .filter(entity -> entity.getAccId().longValue() == singleCount.getAccId())
                .forEach(bill -> {
                    bill.setSurplusBounty(bill.getSurplusBounty() == null ? new BigDecimal(0) : bill.getSurplusBounty());
                    bill.setValalidBounty(bill.getValalidBounty() == null ? new BigDecimal(0) : bill.getValalidBounty());
                    if (bill.isValid()) {
                        singleCount.setUsable(singleCount.getUsable().add(bill.getSurplusBounty()));
                        singleCount.setWillExpire(singleCount.getWillExpire().add(bill.getValalidBounty()));
                        if(singleCount.getWillExpire().doubleValue()<0){
                            singleCount.setWillExpire(new BigDecimal(0));
                        }
                    }
                });
            });
        }

        return singleCounts;
    }

    /**
     * 获取第n天
     */
    public static Date getNextDay(Date date, int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, n);//+1今天的时间加一天
        date = calendar.getTime();
        return date;
    }

    /**
     * date格式转string
     * @param date
     * @return
     */
    public String dateConvertString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd ");
        return formatter.format(date);
    }


    @Override
    public List<SingleConsumeDetail> ConsumeDetail(Query query) {
        return hxBountyDao.ConsumeDetail(query);
    }

    @Override
    public int dealerTotal(Query query) {

        return hxBountyDao.dealerTotalFirst(query);
    }

    @Override
    public int singleTotal(SysUserEntity user, Query query) {
        if (!user.isSuperAdmin()) {
            if (null != user.getDealerId()) {
                query.put("dealerId", user.getDealerId());
            } else {
                query.put("orgIds", getOrgsByUser(user, query));
            }
        }
        return hxBountyDao.singleTotal(query);
    }

    @Override
    public int ConsumeDetailTotal(Query query) {
        return hxBountyDao.ConsumeDetailTotal(query);
    }

    private List<BmSysOrganization> getOrgsByUser(SysUserEntity user, Query query) {

        List<BmSysOrganization> list = bmOrganizationService.queryListByUser(user);
        return list;
    }

    @Override
    public int getOrgsSizeByUser(SysUserEntity user, Query query) {
        if (!user.isSuperAdmin()) {
            if (null != user.getDealerId()) {
                query.put("dealerId", user.getDealerId());
            } else {
                query.put("orgIds", getOrgsByUser(user, query));
            }
        }
        return hxBountyDao.orgTotalFirst(query);
    }

    private String cuurentDay() {
        LocalDateTime l = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss");
        return df.format(l);
    }
}

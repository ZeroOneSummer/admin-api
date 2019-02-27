package com.dms.api.modules.service.report.bean.impl;

import com.dms.api.common.utils.Query;
import com.dms.api.common.utils.TimeUtil;
import com.dms.api.modules.dao.report.bean.BountyDailySettlementDao;
import com.dms.api.modules.dao.report.bean.HxBountyDao;
import com.dms.api.modules.entity.report.bean.BountyDailySettlementEntity;
import com.dms.api.modules.entity.report.bean.DealerCount;
import com.dms.api.modules.entity.report.bean.OrgCount;
import com.dms.api.modules.service.report.bean.BountyDailySettlementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service("bountyDailySettlementService")
public class BountyDailySettlementServiceImpl implements BountyDailySettlementService {

    @Autowired
    private BountyDailySettlementDao bountyDailySettlementDao;

    @Autowired
    private HxBountyDao hxBountyDao;

    @Override
    public BountyDailySettlementEntity queryObject(Long id) {
        return bountyDailySettlementDao.queryObject(id);
    }

    private String cuurentDay() {
        LocalDateTime l = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss");
        return df.format(l);
    }

    @Override
    public List<BountyDailySettlementEntity> queryListByOrg(Map<String, Object> map) {
        List<BountyDailySettlementEntity> list = new ArrayList<>();
        String dealerId = null == map.get("dealerId") ? null : map.get("dealerId") + "";
        String orgId = null == map.get("orgId") ? null : map.get("orgId") + "";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String nowSt = dtf.format(LocalDateTime.now());
        BigDecimal inital = new BigDecimal(0);
        String minDate = null != dealerId ? hxBountyDao.getMinDateByDealerId(map) : hxBountyDao.getMinDateByOrgId(map);
        map.put("minDate", minDate);
        if ("".equals(minDate) || null == minDate){
            return list;
        }
        List<String> dateList = TimeUtil.getDateList(map);
        map.put("list",dateList);
        list.addAll(bountyDailySettlementDao.queryList(map));
        //去除已存在记录的预订日
//        list.forEach(entity -> dateList.remove(entity.getTradingDay()));

        List<String> curDateList=new ArrayList<>();
        String currentDay = org.apache.commons.lang3.StringUtils.substring(cuurentDay(),0,10);//当天
        for(String date:dateList){
            if(!org.apache.commons.lang3.StringUtils.equals(date,currentDay)){
                long count = list.stream().filter(entity -> date.equals(entity.getTradingDay())).count();
                if(count<=0){//没找到
                    BountyDailySettlementEntity settlementEntity = new BountyDailySettlementEntity(dealerId, orgId, date, inital, inital, inital, inital);
                    list.add(settlementEntity);
                }
            }else{
                curDateList.add(date);
            }

        }

        if (0 < curDateList.size()) {//如果包含当天 则查询当天数据
            if(org.apache.commons.lang3.StringUtils.isNotBlank(orgId)){
                Query query=new Query();
                List<OrgCount> orgList=new ArrayList<>();
                OrgCount orgCount=new OrgCount();
                orgCount.setOrgId(Long.valueOf(orgId));
                orgList.add(orgCount);
                query.put("orgList",orgList);
                //若查询时间包括当天则统计流水中当天的数据
                List<OrgCount> incomeAndConsumeList = hxBountyDao.countCurrentByOrgId(query);//收入支出
                List<OrgCount> invalidList = hxBountyDao.countCurrentInvalidByOrgId(query);//失效

                BigDecimal total=new BigDecimal(0);//收入
                BigDecimal consume=new BigDecimal(0); //支出
                BigDecimal invalid=new BigDecimal(0); //失效

                if(incomeAndConsumeList!=null&&!incomeAndConsumeList.isEmpty()){
                    total=incomeAndConsumeList.get(0).getTotal();
                    consume=incomeAndConsumeList.get(0).getConsume();
                }
                if(invalidList!=null&&!invalidList.isEmpty()){
                    invalid=invalidList.get(0).getInvalid();
                }

                BountyDailySettlementEntity settlementEntity = new BountyDailySettlementEntity(dealerId, orgId, currentDay, total, total, consume, invalid);
                list.add(settlementEntity);
            }else{
                BountyDailySettlementEntity settlementEntity = new BountyDailySettlementEntity(dealerId, orgId, currentDay, inital, inital, inital, inital);
                list.add(settlementEntity);
            }
        }

        Comparator<BountyDailySettlementEntity> comparator = Comparator.comparing(BountyDailySettlementEntity::getTradingDay);
        list.sort(comparator.reversed());
        return list;
    }

    @Override
    public List<BountyDailySettlementEntity> queryListByDealer(Map<String, Object> map) {
        List<BountyDailySettlementEntity> list = new ArrayList<>();
        String dealerId = null == map.get("dealerId") ? null : map.get("dealerId") + "";
        String orgId = null == map.get("orgId") ? null : map.get("orgId") + "";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String nowSt = dtf.format(LocalDateTime.now());
        BigDecimal inital = new BigDecimal(0);
        String minDate = null != dealerId ? hxBountyDao.getMinDateByDealerId(map) : hxBountyDao.getMinDateByOrgId(map);
        map.put("minDate", minDate);
        if ("".equals(minDate) || null == minDate){
            return list;
        }
        List<String> dateList = TimeUtil.getDateList(map);
        map.put("list",dateList);
        list.addAll(bountyDailySettlementDao.queryList(map));
        //去除已存在记录的预订日
//        list.forEach(entity -> dateList.remove(entity.getTradingDay()));

        List<String> curDateList=new ArrayList<>();
        String currentDay = org.apache.commons.lang3.StringUtils.substring(cuurentDay(),0,10);//当天
        for(String date:dateList){
            if(!org.apache.commons.lang3.StringUtils.equals(date,currentDay)){
                long count = list.stream().filter(entity -> date.equals(entity.getTradingDay())).count();
                if(count<=0){//没找到
                    BountyDailySettlementEntity settlementEntity = new BountyDailySettlementEntity(dealerId, orgId, date, inital, inital, inital, inital);
                    list.add(settlementEntity);
                }
            }else{
                curDateList.add(date);
            }
        }

        if (0 < curDateList.size()) {//如果包含当天 则查询当天数据
            if(org.apache.commons.lang3.StringUtils.isNotBlank(dealerId)){
                Query query=new Query();
                List<DealerCount> dealerList=new ArrayList<>();
                DealerCount dealerCount=new DealerCount();
                dealerCount.setDealerId(Long.valueOf(dealerId));
                dealerList.add(dealerCount);
                query.put("dealerList",dealerList);
                //若查询时间包括当天则统计流水中当天的数据
                List<DealerCount> incomeAndConsumeList = hxBountyDao.countCurrentByDealerId(query);//收入支出
                List<DealerCount> invalidList = hxBountyDao.countCurrentInvalidByDealerId(query);//失效

                BigDecimal total=new BigDecimal(0);//收入
                BigDecimal consume=new BigDecimal(0); //支出
                BigDecimal invalid=new BigDecimal(0); //失效

                if(incomeAndConsumeList!=null&&!incomeAndConsumeList.isEmpty()){
                    total=incomeAndConsumeList.get(0).getTotal();
                    consume=incomeAndConsumeList.get(0).getConsume();
                }
                if(invalidList!=null&&!invalidList.isEmpty()){
                    invalid=invalidList.get(0).getInvalid();
                }

                BountyDailySettlementEntity settlementEntity = new BountyDailySettlementEntity(dealerId, orgId, currentDay, total, total, consume, invalid);
                list.add(settlementEntity);
            }else{
                BountyDailySettlementEntity settlementEntity = new BountyDailySettlementEntity(dealerId, orgId, currentDay, inital, inital, inital, inital);
                list.add(settlementEntity);
            }
        }

        Comparator<BountyDailySettlementEntity> comparator = Comparator.comparing(BountyDailySettlementEntity::getTradingDay);
        list.sort(comparator.reversed());
        return list;
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        String minDate;
        if (null != map.get("dealerId")) {
            minDate = hxBountyDao.getMinDateByDealerId(map);
        } else {
            minDate = hxBountyDao.getMinDateByOrgId(map);
        }
        if ("".equals(minDate) || null == minDate) {
            return 0;
        }
        map.put("limit", null);
        map.put("page", null);
        map.put("minDate", minDate);
        return TimeUtil.getDateList(map).size();
    }

    @Override
    public void save(BountyDailySettlementEntity bountyDailySettlement) {
        bountyDailySettlementDao.save(bountyDailySettlement);
    }

    @Override
    public void update(BountyDailySettlementEntity bountyDailySettlement) {
        bountyDailySettlementDao.update(bountyDailySettlement);
    }

    @Override
    public void delete(Long id) {
        bountyDailySettlementDao.delete(id);
    }

    @Override
    public void deleteBatch(Long[] ids) {
        bountyDailySettlementDao.deleteBatch(ids);
    }

}

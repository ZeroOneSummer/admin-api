package com.dms.api.modules.service.reserve.coupons.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.dms.api.common.utils.R;
import com.dms.api.common.utils.StringUtils;
import com.dms.api.modules.dao.reserve.coupons.CouponCheckDao;
import com.dms.api.modules.dao.reserve.coupons.CouponDetailDao;
import com.dms.api.modules.dao.reserve.coupons.CouponUseLogsDao;
import com.dms.api.modules.dao.sys.user.SysUserDao;
import com.dms.api.modules.entity.reserve.coupons.*;
import com.dms.api.modules.entity.sys.user.SysUserEntity;
import com.dms.api.modules.service.reserve.coupons.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CouponDetailServiceImpl implements CouponDetailService {
    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private CouponDetailDao couponDetailDao;

    @Autowired
    private CouponCheckDao couponCheckDao;

    @Autowired
    private CouponWarehouseService couponWarehouseService;

    @Autowired
    private CouponConditionService couponConditionService;

    @Autowired
    private CouponUseLogsService couponUseLogsService;

    @Autowired
    private CouponOverdueService couponOverdueService;

    @Autowired
    private SysUserDao sysUserDao;

    @Autowired
    private CouponUseLogsDao couponUseLogsDao;

    @Override
    public CouponDetail queryObject(Integer id) {
        return couponDetailDao.queryObject(id);
    }

    @Override
    public List<CouponDetail> queryList(Map<String, Object> map) {
        return couponDetailDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return couponDetailDao.queryTotal(map);
    }

    @Override
    public R save(CouponDetail couponDetail) {

        //获取用户信息
        SysUserEntity user = getUser();
        //服务商校验
        boolean flag = true; // 默认是超管
        String loginCode = null;
        Map<String, Object> map2 = null; //根据dealerId查用户对应accId和loginCode
        if (!user.isSuperAdmin() && !user.isSysSuperAdmin()) { //服务商
            flag = false;
            if (user.getDealerId() == null) {
                logger.error("优惠券生成失败,服务商不存在!");
                return R.error("优惠券生成失败,服务商不存在!");
            } else {
                couponDetail.setDealerId(user.getDealerId().toString()); //服务商ID
                map2 = couponDetailDao.getLoginCodeByDealerId(couponDetail.getDealerId());
                loginCode = map2 == null ? null : (String) map2.get("loginCode");
            }
        } else { //验证管理员输入的dealerId是否存在
            map2 = couponDetailDao.getLoginCodeByDealerId(couponDetail.getDealerId());
            loginCode = map2 == null ? null : (String) map2.get("loginCode");
        }
        if (map2 == null||map2.size()<=0) return R.error("操作失败,服务商信息不存在");
        couponDetail.setCreateUser(user.getUserId().toString()); //创建者
//        final String userName = flag ? "admin" : loginCode; //管理员没loginCode

        //生成预待审核券
        this.createCoupons(couponDetail);

        return R.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R update(CouponDetail couponDetail) {

        //验证券是否已审核通过(防止前端验证通过后再审核)
        String status = couponDetailDao.couponIsAudit(couponDetail.getCouponNo());
        if ("1".equals(status)) {
            return R.error("该券已审核，无法修改");
        }

        //获取用户信息
        SysUserEntity user = getUser();
        //服务商校验
        boolean flag = true; // 默认是超管
        String loginCode = null;
        Map<String, Object> map = null;
        if (!user.isSuperAdmin() && !user.isSysSuperAdmin()) { //服务商
            flag = false;
            if (user.getDealerId() == null) {
                logger.error("更新失败，服务商不存在");
                return R.error("更新失败，服务商不存在");
            } else {
                map = couponDetailDao.getLoginCodeByDealerId(user.getDealerId().toString());
                loginCode = map == null ? null : (String) map.get("loginCode");
            }
        } else {
            map = couponDetailDao.getLoginCodeByDealerId(couponDetail.getDealerId());
            loginCode = map == null ? null : (String) map.get("loginCode");
        }
        if (loginCode == null) return R.error("更新失败,服务商或服务商logincode不存在!");
        final String userName = flag ? "admin" : loginCode;


        if (!"1".equals(couponDetail.getStatus())) { //券更新为失效
            //插入日志表
            this.insertLogByUpdate(couponDetail, user.getUsername());

            //插入失效表
            this.insertOverDue(couponDetail);

            //查询父IDs对应的子IDs
            Integer[] ids = new Integer[]{couponDetail.getId()};
            Integer[] ids3 = this.getIds(ids, "coupon_warehouse");

            //批量更新仓库表（初始化状态全部改为已销毁）
            if(ids3 != null && ids3.length > 0){
                logger.info("开始批量更新仓库表coupon_warehouse");
                couponWarehouseService.updateBatch(ids3);
            }

            //更新券基础表
            logger.info("开始更新券基础表coupon_detail");
            couponDetailDao.update(couponDetail);

        } else {//券更新为有效
            //更新券基础表，仓库表不变
            logger.info("非状态字段更改，开始更新券基础表coupon_detail");
            couponDetailDao.update(couponDetail);
        }

        logger.info("更新成功");
        return R.ok();
    }

    @Override
    public void delete(Integer id) {
        couponDetailDao.delete(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R deleteBatch(Integer[] ids) {

        //查询父IDs对应的子IDs
        Integer[] ids3 = this.getIds(ids, "coupon_warehouse");
        //删除子IDs(仓库表)
        if (ids3 != null) couponWarehouseService.deleteBatch(ids3);

        //查询父IDs对应的子IDs
        Integer[] ids4 = this.getIds(ids, "coupon_condition");
        //删除子IDs(条件限制表)
        if (ids4 != null) couponConditionService.deleteBatch(ids4);

        //删除父IDs
        couponDetailDao.deleteBatch(ids);

        logger.info("删除成功");
        return R.ok();

    }


    /**
     * 手动发券
     *
     * @param dealerId
     * @param loginCodes
     */
    @Transactional
    public R grantCoupon(String dealerId, String couponNo, String loginCodes) {
        logger.info("手动发券 dealerId:{},  loginCodes:{}", dealerId, loginCodes);

        String[] loginCodesArry = StringUtils.split(loginCodes, ",");

        //1.找出符合该服务商下面的loginCode
        List<DealerLoginCode> dealerLoginCodes = couponDetailDao.queryDealerLoginCode(dealerId, loginCodesArry);
        if (dealerLoginCodes == null || dealerLoginCodes.isEmpty()) {
            return R.error("loginCode不是券所在服务商客户");
        }

        //查出可发放得券
        List<CouponDetail> couponDetails = couponDetailDao.userSureReceiveList(dealerId, couponNo);
        if (couponDetails == null || couponDetails.isEmpty()) {
            return R.error("没有券可发放");
        }

        //找出用户拥有的券数量
        List<CouponLoginCodeCount> couponLoginCodeCounts = couponDetailDao.queryCouponLoginCodeCount(couponNo);
        Map<String,Integer> couponLoginCodeCountsMap = couponLoginCodeCounts.stream().collect(Collectors.toMap(CouponLoginCodeCount::getLoginCode,
                CouponLoginCodeCount::getCount));

        int idx = dealerLoginCodes.size();
        if (couponDetails.size() < dealerLoginCodes.size()) {//如果系统中可发放得券 数量小于 要发放的数量 则以系统中的券数量为主
            idx = couponDetails.size();
        }

        List<CouponUseLogs> couponUseLogs = new ArrayList<>();
        DealerLoginCode dealerLoginCode = null;
        CouponDetail couponDetail = null;
        int couponCount=0;
        Integer coupongLoginCodeCount=null;
        for (int i = 0; i < idx; i++) {
            dealerLoginCode = dealerLoginCodes.get(i);
            couponDetail = couponDetails.get(i);

            coupongLoginCodeCount=couponLoginCodeCountsMap.get(dealerLoginCode.getLoginCode());
            if(coupongLoginCodeCount!=null&&coupongLoginCodeCount.intValue()>=couponDetail.getObtainLimit().intValue()){//如果当前用户拥有得券数量超过 券本身限制得数量
               logger.info("loginCode:{} couponNo:{} 超过券发放数量限制",dealerLoginCode.getLoginCode(),couponNo);
                continue;
            }
            String couponGetInvalidDay = couponDetail.getCouponGetInvalidDay();
            String couponUseEndDate="";
            if(StringUtils.isNotBlank(couponGetInvalidDay)){
               int  couponGetInvalidDayInt=Integer.parseInt(couponGetInvalidDay)-1;
                if(couponGetInvalidDayInt<=0){
                    couponGetInvalidDayInt=0;
                }
                couponUseEndDate=DateUtil.date().offset(DateField.DAY_OF_MONTH,couponGetInvalidDayInt).toString("yyyy-MM-dd");
            }

            //如果券领取后最晚时间 超过券有效期的截止时间则以券有效期截止时间为准
            if(StringUtils.isNotBlank(couponUseEndDate)){
                Date start=DateUtil.parse(couponUseEndDate,"yyyy-MM-dd");
                Date end=DateUtil.parse(couponDetail.getEndDate(),"yyyy-MM-dd");
                long betweenDay = DateUtil.between(start, end, DateUnit.DAY,false);
                if(betweenDay<0){
                    couponUseEndDate= couponDetail.getEndDate();
                }
            }


            int rows= couponDetailDao.updateReceiveCoupon(dealerLoginCode.getLoginCode(), couponDetail.getCouponSn(),couponUseEndDate);
            if(rows<=0){//如果没有更新行数
              continue;
            }

            //券使用日志
            CouponUseLogs couponUseLog = new CouponUseLogs();
            couponUseLog.setCouponSn(couponDetail.getCouponSn());
            couponUseLog.setUseType("1");//已领取
            couponUseLog.setUseUser(dealerLoginCode.getLoginCode());
            couponUseLog.setOrderCode("");
            couponUseLogs.add(couponUseLog);

            couponCount++;
        }

        //券使用日志
        if (!couponUseLogs.isEmpty()) {
            couponUseLogsDao.insertBatchCouponUseLogs(couponUseLogs);
        }
        return R.ok("已发券:" + couponCount + "张");
    }


    @Override
    public String getWarehouseIdsByIds(Integer[] ids) {
        return couponDetailDao.getWarehouseIdsByIds(ids);
    }

    @Override
    public String getConditionIdsByIds(Integer[] ids) {
        return couponDetailDao.getConditionIdsByIds(ids);
    }

    @Override
    public Map<String, Object> getLoginCodeByDealerId(String dealerId) {
        return couponDetailDao.getLoginCodeByDealerId(dealerId);
    }

    /**
     * 获取券密钥
     */
    private String getSecretKey(String param) {
        return DigestUtils.md5Hex(param);
    }

    /**
     * 获取用户信息
     */
    @Override
    public SysUserEntity getUser() {
        SysUserEntity user = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        SysUserEntity sysUserEntity = sysUserDao.queryObject(user.getUserId());
        if (sysUserEntity != null) {
            user.setDealerId(sysUserEntity.getDealerId()); //根据userId重新查dealerId
        }
        return user;
    }

    /**
     * 根据couponDetail的id查询对应的关联表的ids
     */
    private Integer[] getIds(Integer[] ids, String getType) {
        Integer[] ids3 = null;
        String strIds = null;
        if ("coupon_warehouse".equals(getType)) {
            strIds = couponDetailDao.getWarehouseIdsByIds(ids);
        } else if ("coupon_condition".equals(getType)) {
            strIds = couponDetailDao.getConditionIdsByIds(ids);
        }
        if (strIds != null && strIds != "") {
            String[] ids2 = strIds.split(",");
            ids3 = new Integer[ids2.length];
            for (int i = 0; i < ids3.length; i++) {
                ids3[i] = Integer.parseInt(ids2[i]);
            }
        }
        return ids3;
    }

    /**
     * 服务商生成优惠券
     */
    private void createCoupons(CouponDetail couponDetail) {
        String couponNo = "Q" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()); //券批次号(券序列号 = 券批次号-i)
        String couponPass = null; //getSecretKey(couponNo + "89dfsad5g4f"); //券的密钥 = md5(券批次号 + 加盐)
        couponDetail.setCouponNo(couponNo);
        couponDetail.setCreateTime(new Date()); //创建时间
        couponDetail.setTotalPrice(couponDetail.getPrice().multiply(new BigDecimal(couponDetail.getCouponNumber()))); //券总额=面额*券发放数量
        logger.info("开始插入券基础表coupon_detail");
        couponDetailDao.save(couponDetail);
    }

    /**
     * 插入失效表
     */
    private void insertOverDue(CouponDetail couponDetail) {
        Map<String, Object> map3 = new HashMap<>();
        map3.put("couponNo", couponDetail.getCouponNo()); //更新的券批次号
        map3.put("useStatus", new String[]{"0"}); //更新的券（券状态为 “ 0-初始化状态 ” 需更新）
        List<CouponWarehouse> list4 = couponWarehouseService.queryList(map3);
        List<CouponOverdue> list5 = new ArrayList<>();

        list4.stream().forEach(warehouse -> {
            CouponOverdue overdues = new CouponOverdue();
            overdues.setCouponSn(warehouse.getCouponSn());
            overdues.setOverdueMoney(warehouse.getPrice());
            overdues.setCreDate(new Date());
            overdues.setHasTransfer("0"); //是否已划转 1:是 0否
            overdues.setType("-1"); //销毁
            list5.add(overdues);
        });
        if(list5 != null && list5.size() > 0){
            logger.info("开始插入失效表coupon_overdue");
            couponOverdueService.saveBatch(list5);
        }
    }

    /**
     * 仓库表更新时插入日志表
     */
    private void insertLogByUpdate(CouponDetail couponDetail, String userName) {
        Map<String, Object> map2 = new HashMap<>();
        map2.put("couponNo", couponDetail.getCouponNo()); //更新的券批次号
        map2.put("useStatus", new String[]{"0"}); //更新的券（券状态为 “ 0-初始化状态 ” 需更新）
        List<CouponWarehouse> list = couponWarehouseService.queryList(map2);
        List<CouponUseLogs> list2 = new ArrayList<>();

        list.stream().forEach(warehouse -> {
            CouponUseLogs logs = new CouponUseLogs();
            logs.setCouponSn(warehouse.getCouponSn());
            logs.setUseType("-1"); //销毁
            logs.setUseTime(new Date());
            logs.setUseUser(userName);
            list2.add(logs);
        });
        if(list2 != null && list2.size() > 0){
            logger.info("开始插入日志表coupon_use_logs");
            couponUseLogsService.saveBatch(list2);
        }
    }

    /**
     * 添加待审核记录，并更新预待审核券状态
     */
    @Transactional
    public void addCouponVerifyRecord(CouponDetail couponDetail) {
        //更新预待审核券状态
        couponDetail.setStatus("0.5"); //待审核
        int count=couponDetailDao.updateVerifyStatus(couponDetail);
        if(count<=0){
          throw new RuntimeException("提交审核失败,确认是否已经提交");
        }

        //添加待审核记录
        CouponCheck couponCheck = new CouponCheck();
        couponCheck.setCouponNo(couponDetail.getCouponNo());
        couponCheck.setStatus("0"); //待审核
        couponCheckDao.addCouponVerifyRecord(couponCheck);
    }

}
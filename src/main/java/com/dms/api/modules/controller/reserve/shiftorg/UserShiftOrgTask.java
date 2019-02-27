package com.dms.api.modules.controller.reserve.shiftorg;

import com.alibaba.fastjson.JSONObject;
import com.dms.api.common.exception.DMException;
import com.dms.api.common.ssl.Openssl;
import com.dms.api.common.utils.CreateWhereSQL;
import com.dms.api.common.utils.StringUtils;
import com.dms.api.core.config.HostsConfig;
import com.dms.api.core.constants.ConstantTable;
import com.dms.api.modules.dao.reserve.coupons.CouponDetailDao;
import com.dms.api.modules.entity.reserve.coupons.CouponOverdue;
import com.dms.api.modules.entity.reserve.coupons.CouponUseLogs;
import com.dms.api.modules.entity.reserve.coupons.CouponWarehouse;
import com.dms.api.modules.entity.reserve.shiftorg.ShiftOrgRecordEntity;
import com.dms.api.modules.service.reserve.shiftorg.ShiftOrgRecordService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.dms.api.common.utils.HttpClientUtil.doPostJson;

/**
 * @author 40
 * date 2018/1/10
 * time 13:43
 * decription:用户转移机构任务
 */
@Component("userShiftOrgTask")
public class UserShiftOrgTask {
    private final static Logger logger = Logger.getLogger(UserShiftOrgTask.class);

    @Autowired
    ShiftOrgRecordService shiftOrgRecordService;

    List<Map<String, Object>> ordercouts = null;

    @Autowired
    CouponDetailDao couponDetailDao;

    private Lock lock = new ReentrantLock();

    //转移截止时间
    public static Long deadline = 0L;
    //次日执行时间
    public static Date tomorrow = null;

    public static String validateOrder = "true";


    /**
     * 定时任务执行机构转移
     */
    public void shiftOrg() {
        logger.info("shiftOrg start。。。。");
        validateOrder = "true";
        timingShiftOrg();
    }


    /**
     * 异步执行 定时任务执行机构转移
     */
    @Async
    public void asyncShiftOrg(String flag) {
        logger.info("asyncShiftOrg start。。。。");
        validateOrder = flag;
        timingShiftOrg();
    }


    /**
     * 无时间限制立即执行机构转移
     */
    public void unLimitedShiftOrg() {
        logger.info("unLimitedShiftOrg start。。。。");
        immediatelyShiftOrg();
    }


    /**
     * 异步执行 无时间限制立即执行机构转移
     */
    @Async
    public void asyncUnLimitedShiftOrg() {
        logger.info("asyncUnLimitedShiftOrg start。。。。");
        immediatelyShiftOrg();
    }


    /**
     * 定时机构转移方法
     */
    private void timingShiftOrg() {
        logger.info("============定时机构转移任务开始启动====================");
        TaskInitializing.initUserShiftOrg();
        lock.lock();
        try {

            if (0L == deadline || null == tomorrow) {
                throw new DMException("转移截止时间：" + deadline + ",次日执行时间:" + JSONObject.toJSONString(tomorrow));
            }

            //查询待转移的名单
            List<ShiftOrgRecordEntity> list = shiftOrgRecordService.queryShiftList();

            //查询每个logincode的订单情况
            orderCount(list);

            if (list.size() > 0) {
                for (ShiftOrgRecordEntity entity : list) {
                    Date shiftTime = new Date();
                    String loginCode = entity.getLoginCode();
                    if (loginCode != null) {
                        //如果执行时间超过截止日期则延期至次日执行
                        if (shiftTime.getTime() < deadline) {

                            if (!canShiftOrg(loginCode)) {
                                entity.setStatus(-1);
                                entity.setShiftTime(shiftTime);
                                entity.setRemarks("当日有过下单，处理失败");
                            } else {

                                logger.info("【" + entity.getLoginCode() + "】执行机构转移");
                                entity.setShiftTime(shiftTime);
                                try {
                                    JSONObject result = doShiftOrg(entity);
                                    logger.info("【" + entity.getLoginCode() + "】转移结果：" + result.toJSONString());
                                    if ("0".equals(result.get("error_code"))) {
                                        entity.setStatus(1);

                                        try {
                                            couponFailure(loginCode,entity.getSerialCode());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            logger.info("转移机构，券置为失效异常");
                                        }


                                    } else {
                                        entity.setStatus(-1);
                                    }
                                    entity.setRemarks(result.getString("error_text"));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    logger.error("机构转移处理异常" + e.getMessage());
                                    //若调用接口出现异常则为处理中
                                    entity.setStatus(2);
                                    entity.setRemarks("机构转移接口调用异常");
                                }
                            }
                        } else {
                            entity.setShiftTime(tomorrow);
                        }
                    } else {
                        //若loginCode为空，则未找到该用户
                        entity.setStatus(-1);
                        entity.setShiftTime(shiftTime);
                        entity.setRemarks("该用户不存在");
                    }
                }
                shiftOrgRecordService.updateBatch(list);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            lock.unlock();
        }

        logger.info("============定时机构转移任务开始结束====================");
    }


    /**
     * 立即执行机构转移
     */
    private void immediatelyShiftOrg() {
        logger.info("============立即执行机构转移任务开始启动====================");
        lock.lock();
        try {
            //查询待转移的名单
            List<ShiftOrgRecordEntity> list = shiftOrgRecordService.queryShiftList();

            //查询每个logincode的订单情况
            orderCount(list);

            if (list.size() > 0) {
                list.forEach(entity -> {
                    Date shiftTime = new Date();
                    String loginCode = entity.getLoginCode();
                    if (loginCode != null) {
                        if (!canShiftOrg(loginCode)) {
                            //下一天转移
//                            entity.setShiftTime(tomorrow);
                            entity.setStatus(-1);
                            entity.setShiftTime(shiftTime);
                            entity.setRemarks("当日有过下单，处理失败");
                        } else {


                            logger.info("【" + entity.getLoginCode() + "】执行机构转移");
                            entity.setShiftTime(shiftTime);
                            try {
                                JSONObject result = doShiftOrg(entity);
                                logger.info("【" + entity.getLoginCode() + "】转移结果：" + result.toJSONString());
                                if ("0".equals(result.get("error_code"))) {
                                    entity.setStatus(1);
                                    try {
                                        couponFailure(loginCode,entity.getSerialCode());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        logger.info("转移机构，券置为失效异常");
                                    }
                                } else {
                                    entity.setStatus(-1);
                                }
                                entity.setRemarks(result.getString("error_text"));
                            } catch (Exception e) {
                                e.printStackTrace();
                                //若调用接口出现异常则为处理中
                                entity.setStatus(2);
                                entity.setRemarks("机构转移接口调用异常");
                            }
                        }
                    } else {
                        //若loginCode为空，则未找到该用户
                        entity.setStatus(-1);
                        entity.setShiftTime(shiftTime);
                        entity.setRemarks("该用户不存在");
                    }

                });
                shiftOrgRecordService.updateBatch(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        logger.info("============立即执行机构转移任务开始结束====================");
    }


    /**
     * 执行转移机构
     *
     * @param record
     * @return
     */
    private JSONObject doShiftOrg(ShiftOrgRecordEntity record) {
        JSONObject json = new JSONObject();
        json.put("logincode", record.getLoginCode());
        json.put("serialcode", record.getSerialCode());
        json.put("sign", Openssl.sgin(JSONObject.toJSONString(json)));
        logger.info("调用接口转移 url" + (HostsConfig.hostsB + ConstantTable.USER_ORG_MOVE) + "   参数:" + json.toJSONString());
        String result = doPostJson(HostsConfig.hostsB + ConstantTable.USER_ORG_MOVE, json.toJSONString());
        return JSONObject.parseObject(result);
    }

    /**
     * 当前是否可以转移机构，不能转移时将直接定义为转移失败。
     *
     * @param logincode
     * @return
     */
    public boolean canShiftOrg(String logincode) {
        if (null != ordercouts && ordercouts.size() > 0) {
            for (Map<String, Object> item : ordercouts) {
                String logincodeTemp = (String) item.get("logincode");
                if (logincode.equals(logincodeTemp)) {
                    Long count = (Long) item.get("count");

                    return !(count > 0);
                }
            }
        }
        return true;
    }

    /**
     * 获取订单情况
     *
     * @param list
     * @return
     */
    public List<Map<String, Object>> orderCount(List<ShiftOrgRecordEntity> list) {
        if (null != list && list.size() > 0) {
            List<String> logincodes = new ArrayList<>(list.size());
            for (ShiftOrgRecordEntity shift : list) {
                String logincode = shift.getLoginCode();
                logincodes.add(logincode);
            }

            Map<String, Object> param = new HashMap<>(2);
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            if (hour >= 4 && hour <= 8) { //定时转机构
                Map<String, Object> timeType = new HashMap<>(1);
                timeType.put("timeType", CreateWhereSQL.TIME_TYPE_YESTERODAY);
                Map<String, Object> stringObjectMap = CreateWhereSQL.get(timeType);
                param.put("startTime", stringObjectMap.get("startTime"));
                param.put("endTime", stringObjectMap.get("endTime"));
                if (org.apache.commons.lang.StringUtils.equalsIgnoreCase(validateOrder, "true")) {
                    logger.info("定时机构转移 校验当前是否存在未退订单据");
                    ordercouts = shiftOrgRecordService.orderCount2(logincodes, param);
                }
            } else {  //立即转机构
                Map<String, Object> timeType = new HashMap<>(1);
                timeType.put("timeType", CreateWhereSQL.TIME_TYPE_CURRENTDAY);
                Map<String, Object> stringObjectMap = CreateWhereSQL.get(timeType);
                param.put("startTime", stringObjectMap.get("startTime"));
                param.put("endTime", stringObjectMap.get("endTime"));
                ordercouts = shiftOrgRecordService.orderCount(logincodes, param);
            }

            return ordercouts;

        }

        return null;
    }


    @Transactional
    public void couponFailure(String loginCode,String serialCode) {
        if (StringUtils.isBlank(loginCode)) {
            logger.info("机构转移 loginCode 为空");
            return;
        }
        if (StringUtils.isBlank(serialCode)) {
            logger.info("机构转移 serialCode 为空");
            return;
        }
        //根据推荐码获取服务商编号
        String dealerCode = couponDetailDao.queryDealerCodeBySequence(serialCode);

        List<CouponWarehouse> couponWarehouses = couponDetailDao.queryUserCoupon(loginCode);
        if (couponWarehouses == null || couponWarehouses.isEmpty()) {
            logger.info("机构转移 logincode:" + loginCode + " 没有券");
            return;
        }

        List<String> couponSnList = new ArrayList<>();
        List<CouponUseLogs> couponUseLogs = new ArrayList<>();
        List<CouponOverdue> coList = new ArrayList<>();
        for (CouponWarehouse cw : couponWarehouses) {
             //如果转移目标机构的 服务商编号==券对应服务商编号 则券不失效
             if(StringUtils.isNotBlank(dealerCode)&&StringUtils.equalsIgnoreCase(dealerCode,cw.getDealerCode())){
                continue;
             }


            //券过期数据
            CouponOverdue co = new CouponOverdue();
            co.setCouponSn(cw.getCouponSn());
            co.setType("-4");//-4 转移机构失效
            co.setOverdueMoney(cw.getPrice());//失效金额
            co.setHasTransfer("0");//是否已资金划转
            coList.add(co);

            couponSnList.add(cw.getCouponSn());

            //券使用日志
            CouponUseLogs couponUseLog = new CouponUseLogs();
            couponUseLog.setCouponSn(cw.getCouponSn());
            couponUseLog.setUseType("-4");//-4 转移机构失效
            couponUseLog.setUseUser("-1");
            couponUseLog.setOrderCode("");
            couponUseLogs.add(couponUseLog);
        }


        //将券改为已过期
        if (!couponSnList.isEmpty()) {
            couponDetailDao.updateCouponWarehouseStatus("-4", couponSnList);
        }
//        //将券批次改为已过期
//        couponOverdueMapper.updateCouponDetail(couponNo, "-1");//过期失效
        //券使用日志
        if (!couponUseLogs.isEmpty()) {
            couponDetailDao.insertBatchCouponUseLogs(couponUseLogs);
        }
        //新增券过期数据
        if (!coList.isEmpty()) {
            couponDetailDao.insertBatchCouponOverdue(coList);
        }
    }


}

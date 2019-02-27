package com.dms.api.modules.service.mall.order.refund.impl;

import com.alibaba.fastjson.JSONObject;
import com.dms.api.common.exception.DMException;
import com.dms.api.common.utils.HttpClientUtil;
import com.dms.api.common.utils.Query;
import com.dms.api.common.utils.R;
import com.dms.api.core.config.HostsConfig;
import com.dms.api.core.constants.ConstantTable;
import com.dms.api.modules.dao.mall.order.refund.RefundApplyDao;
import com.dms.api.modules.dao.mall.order.refund.VerifyRecordDao;
import com.dms.api.modules.dao.sys.config.SysConfigDao;
import com.dms.api.modules.entity.mall.order.refund.RefundApplyAndVerifyRecordVo;
import com.dms.api.modules.entity.mall.order.refund.RefundApplyEntity;
import com.dms.api.modules.entity.mall.order.refund.VerifyRecordEntity;
import com.dms.api.modules.entity.sys.config.SysConfigEntity;
import com.dms.api.modules.service.mall.order.refund.RefundApplyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service("refundApplyService")
public class RefundApplyServiceImpl implements RefundApplyService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RefundApplyDao refundApplyDao;

    @Autowired
    private VerifyRecordDao verifyRecordDao;

    @Autowired
    SysConfigDao sysConfigDao;

    @Override
    public RefundApplyEntity queryObject(Long id) {
        return refundApplyDao.queryObject(id);
    }

    @Override
    public List<RefundApplyEntity> queryList(Map<String, Object> map) {
        return refundApplyDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return refundApplyDao.queryTotal(map);
    }

    @Override
    public void save(RefundApplyEntity refundApply) {
        refundApplyDao.save(refundApply);
    }

    @Override
    public void update(RefundApplyEntity refundApply) {
        refundApplyDao.update(refundApply);
    }

    @Override
    public void delete(Long id) {
        refundApplyDao.delete(id);
    }

    @Override
    public void deleteBatch(Long[] ids) {
        refundApplyDao.deleteBatch(ids);
    }

    /**
     * @param query
     * @return
     */
    @Override
    public List<RefundApplyAndVerifyRecordVo> refundApplyAndVerifyRecordVoList(Query query) {
        return refundApplyDao.refundApplyAndVerifyRecordVoList(query);
    }

    @Override
    public RefundApplyAndVerifyRecordVo info(Long id, long type) {

        return refundApplyDao.info(id, type);
    }

    /**
     * 审核
     *
     * @param refundApplyAndVerifyRecordVo
     * @param userId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R commitApply(RefundApplyAndVerifyRecordVo refundApplyAndVerifyRecordVo, Long userId) {
        R res = R.ok();
        VerifyRecordEntity v1 = verifyRecordDao.queryObject(refundApplyAndVerifyRecordVo.getVId());
        //订单审核是否存在重复审核
        if(!v1.getType().equals(refundApplyAndVerifyRecordVo.getType())) return R.error("您提交的信息有误，请检查后提交！");
        if (v1.getStatus() != 0) return R.error("请勿重复审核");
        v1.setVerifyBy(userId);
        v1.setVerifyTime(new Date());
        v1.setStatus(refundApplyAndVerifyRecordVo.getStatus());
        int i = verifyRecordDao.updateByReferId(v1);
        if (i <= 0) {
            return R.error("审核失败！系统异常，请联系管理员！");
        }
        //退差价申请信息
        RefundApplyEntity r1 = refundApplyDao.queryObject(refundApplyAndVerifyRecordVo.getId());
        //审核驳回则调用通知商城
        if (refundApplyAndVerifyRecordVo.getStatus() == -1) {
            return remoteRefund(v1, refundApplyAndVerifyRecordVo.getOrderCode());
        }
        int type = refundApplyAndVerifyRecordVo.getType();
        switch (type) {
            //初审
            case 0:
                if (refundApplyAndVerifyRecordVo.getStatus() == 1) {
                    SysConfigEntity sysConfigEntity = sysConfigDao.queryByKey("refundLimit");
                    String topLimitSt = sysConfigEntity == null ? "1000" : sysConfigEntity.getValue();
                    BigDecimal topLimit;
                    try {
                        topLimit = new BigDecimal(topLimitSt);
                    } catch (NumberFormatException e) {
                        logger.error("退差价额度系统参数设置有误！默认退差金额加K豆数量超过1000则需二审");
                        res.put("msg", "审核成功！（退差价额度系统参数设置有误！默认退差金额加K豆数量超过1000则需二审）");
                        topLimit = new BigDecimal(1000);
                    }
                    BigDecimal total = r1.getRefundMoney().add(r1.getRefundBean());

                    Boolean nextVerify = total.compareTo(topLimit) >= 0;
                    VerifyRecordEntity verifyRecordEntity;
                    //若审核通过以及退还金额加K豆超过指定上限则需二审
                    if (nextVerify){
                        verifyRecordEntity = new VerifyRecordEntity(null, 1, 0, null, ConstantTable.REFUND_APPLY_VERIFY, refundApplyAndVerifyRecordVo.getId() + "", null);
                        verifyRecordEntity.setNextVerify(nextVerify);
                    }else {
                        //未达上限则直接到三审
                        verifyRecordEntity = new VerifyRecordEntity(null, 2, 0, null, ConstantTable.REFUND_APPLY_VERIFY, refundApplyAndVerifyRecordVo.getId() + "", null);
                        v1.setStatus(2);
                        verifyRecordDao.updateByReferId(v1);
                    }
                    verifyRecordDao.save(verifyRecordEntity);
                }
                return res;
            //复审
            case 1:
                if (v1.isNextVerify() && refundApplyAndVerifyRecordVo.getStatus() == 2) {
                    //新增三审记录
                    VerifyRecordEntity verifyRecordEntity = new VerifyRecordEntity(null, 2, 0, null, ConstantTable.REFUND_APPLY_VERIFY, refundApplyAndVerifyRecordVo.getId() + "", null);
                    verifyRecordDao.save(verifyRecordEntity);
                }else {
                    throw new DMException("审核失败！", 500);
                }
                return res;
                //三审
            case 2:
                return remoteRefund(v1, r1.getOrderCode());
        }
        return R.error("审核失败！系统异常，请联系管理员！");
    }


    /**
     * 远程调a
     *
     * @param v1
     * @param orderCode
     * @return
     */
    public R remoteRefund(VerifyRecordEntity v1, String orderCode) throws DMException{
        //2为通过执行划转
        String refundStatus = 99 == v1.getStatus() ? "2" : "3";
        Map<String, String> map = new HashMap<>();
        map.put("refundStatus", refundStatus);
        map.put("orderCode", orderCode);
        map.put("token", UUID.randomUUID().toString());
        logger.info("0退差价执行请求参数: " + JSONObject.toJSONString(map)+"  url:"+HostsConfig.hostsA + ConstantTable.REFUND_API);
        String result = HttpClientUtil.doPost(HostsConfig.hostsA + ConstantTable.REFUND_API, map);
        logger.info("0退差价请求返回结果:" + result);
        JSONObject resultJson = JSONObject.parseObject(result);
        int code = Integer.valueOf(resultJson.getString("code"));
        if(code==200){
            return R.ok();
        }
        if(code==10009){//找不到订单号 情况重试第二个链接
            logger.info("1退差价执行请求参数: " + JSONObject.toJSONString(map)+"  url:"+HostsConfig.hostsA + ConstantTable.REFUND_API);
              result = HttpClientUtil.doPost(HostsConfig.hostsA + ConstantTable.REFUND_API, map);
            logger.info("1退差价请求返回结果:" + result);
              resultJson = JSONObject.parseObject(result);
              code = Integer.valueOf(resultJson.getString("code"));
        }



        switch (code){
            case 200:
                return R.ok();
            //订单已经处理，且为已驳回
            case 10000:
                v1.setStatus(-1);
                verifyRecordDao.update(v1);
                return R.ok("审核失败！该申请已经处理过，且为驳回状态。");
            //订单已经处理，且为已退过差价
            case 10001:
                v1.setStatus(99);
                verifyRecordDao.update(v1);
                return R.ok("审核失败！该申请已经处理过，且为同意状态。");
            //A商城异常
            default:
                throw new DMException(resultJson.getString("ret"), code);
        }
    }

    /**
     * 设置查询条件开始，结束时间
     *
     * @param query
     * @param startTime
     * @param endTime
     * @return
     */
    public Query setTime(Query query, String startTime, String endTime) {
        query.put("startTime", startTime + " 00:00:00");
        query.put("endTime", endTime + " 23:59:59");
        return query;
    }

}

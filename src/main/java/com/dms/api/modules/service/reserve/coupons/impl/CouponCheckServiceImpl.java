package com.dms.api.modules.service.reserve.coupons.impl;

import com.alibaba.fastjson.JSONObject;
import com.dms.api.common.utils.CouponException;
import com.dms.api.common.utils.HttpClientUtil;
import com.dms.api.common.utils.StringUtils;
import com.dms.api.core.config.HostsConfig;
import com.dms.api.modules.dao.reserve.coupons.CouponCheckDao;
import com.dms.api.modules.dao.reserve.coupons.CouponDetailDao;
import com.dms.api.modules.entity.reserve.coupons.CouponCheck;
import com.dms.api.modules.entity.reserve.coupons.CouponDetail;
import com.dms.api.modules.entity.reserve.coupons.CouponWarehouse;
import com.dms.api.modules.entity.sys.user.SysUserEntity;
import com.dms.api.modules.service.reserve.coupons.CouponCheckService;
import com.dms.api.modules.service.reserve.coupons.CouponWarehouseService;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CouponCheckServiceImpl implements CouponCheckService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${coupons.delimitFlag:true}")
    private String delimitFlag; //是否划账

    @Autowired
    private CouponCheckDao couponCheckDao;

    @Autowired
    private CouponDetailDao couponDetailDao;

    @Autowired
    private CouponWarehouseService couponWarehouseService;

    @Override
    public CouponDetail queryDetail(String couponNo) {
        return couponCheckDao.queryDetail(couponNo);
    }

    @Override
    public List <CouponCheck> queryList(Map <String, Object> map) {
        return couponCheckDao.queryList(map);
    }

    @Override
    public int queryTotal(Map <String, Object> map) {
        return couponCheckDao.queryTotal(map);
    }


    @Override
    @Transactional
    public void update(CouponCheck couponCheck) {
        //修改审核状态
        couponCheckDao.update(couponCheck);

        CouponDetail couponDetail = new CouponDetail();
        couponDetail.setCouponNo(couponCheck.getCouponNo());
        if("1".equals(couponCheck.getStatus())){ //通过
            couponDetail.setStatus("1"); //审核后更新为【有效】
            couponDetailDao.updateStatus(couponDetail);

            Map<String, Object> payParams = couponCheckDao.queryAccId(couponCheck.getCouponNo());
            if(payParams==null||payParams.size()<=0){
                throw new CouponException("该券没有找到服务商信息");
            }

            SysUserEntity userEntity = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
            //生成券
            this.createCoupons(couponCheck.getCouponNo(),userEntity.getUsername() );

            //调用支付接口
            this.pay(payParams,couponCheck.getLoginPwd(),couponCheck.getPayPwd());

        }else{//驳回
            couponDetail.setStatus("-0.5"); //审核后更新为【审核驳回】
            couponDetailDao.updateStatus(couponDetail);
        }
    }

    /**
     * 服务商生成券(插入仓库表)
     */
    private void createCoupons(String couponNo, String userName) {
        //获取条件，插入到预付款券仓库表&插入日志表
        CouponDetail couponDetail = couponCheckDao.queryDetail(couponNo);

        //插入到预付款券仓库表&插入日志表
        CouponWarehouse couponWarehouse = new CouponWarehouse(couponNo, couponDetail.getCouponNo(), couponDetail.getPrice(), null, "0");
        logger.info("开始插入仓库表coupon_warehouse");
        couponWarehouseService.save(couponWarehouse, couponDetail.getCouponNumber(), userName);
    }

    public void pay(Map<String, Object> payParams,String loginPwd,String monyPwd) {

        //参数验证
        String url = HostsConfig.hostsI;    //支付接口地址
        String srcAccId = payParams == null ? null : (String) payParams.get("srcAccId");
        String loginCode = payParams == null ? null : (String) payParams.get("loginCode");
        BigDecimal amChange = payParams == null ? null : (BigDecimal)payParams.get("amChange");


        logger.info("划账参数 url:{} srcAccId:{}  loginPwd:{}  monyPwd:{}  amChange:{}", url, srcAccId, loginPwd, monyPwd, amChange);
        if (StringUtils.isBlank(srcAccId)) { //accId为空
//            return R.error("优惠券生产失败 accId为空,");
            throw new CouponException("校验划账 accId 为空");
        }

        if (StringUtils.isBlank(loginCode)
                || StringUtils.isBlank(url)
                || StringUtils.isBlank(loginPwd)
                || StringUtils.isBlank(monyPwd)
                || StringUtils.isBlank(srcAccId)) {

//            return R.error("优惠券生产失败,部分参数为空");
            throw new CouponException("校验划账,部分参数为空");
        }

        //==========划账操作==============
        if (StringUtils.equals(delimitFlag, "false")) { //如果明确指定 false表示不调用划账接口 仅在测试调试用，生产必须划账
//            return R.ok();
            return ;
        }

        //==========划账操作==============
        Map<String, String> params = new HashMap<>();
        params.put("loginCode", loginCode); //服务商loginCode
        params.put("loginPwd", loginPwd); //登陆密码
        params.put("type", "0"); //买券
        params.put("role", "S_REPORT"); //调用系统名称
        params.put("rmk1", "服务商生成券资金划转");
        params.put("rmk2", "{no:,sn:Q00000000000000000-000,type:C001}");
        params.put("amChange", amChange.toString()); //划转金额
        params.put("monyPwd", monyPwd); //支付密码
        params.put("srcAccId", srcAccId); //服务商对应得accId

        //支付
        JSONObject jsonObject = this.payCoupon(url, params);

        if (jsonObject == null) {
            throw new CouponException("系统划账异常");
        }
        if (!org.apache.commons.lang.StringUtils.equalsIgnoreCase(jsonObject.getString("rc"), "200")) {
            throw new CouponException("系统划账失败:" + jsonObject.getString("msg"));
        }
    }

    /**
     * 服务商生成优惠券划账接口
     */
    private JSONObject payCoupon(String url, Map<String, String> params) {
        try {
            url = url + "/api/trade/interTranCoupon";
            logger.info("服务商生成预付款券调用划账接口-请求参数 url:{}  param:{}", url, JSONObject.toJSONString(params));
            String result = HttpClientUtil.doPostJson(url, JSONObject.toJSONString(params), 3000);
            logger.info("服务商生成预付款券调用划账接口: 响应:{}", result);
            return JSONObject.parseObject(result);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("服务商生成预付款券调用划账接口异常", e);
        }
        return null;
    }

}
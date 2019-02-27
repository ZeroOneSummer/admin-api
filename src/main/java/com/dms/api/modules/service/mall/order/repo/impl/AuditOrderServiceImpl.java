package com.dms.api.modules.service.mall.order.repo.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dms.api.common.utils.CheckTraPwdUtils;
import com.dms.api.common.utils.HttpClientUtil;
import com.dms.api.common.utils.R;
import com.dms.api.common.utils.StringUtils;
import com.dms.api.core.config.HostsConfig;
import com.dms.api.core.config.SmsConfig;
import com.dms.api.core.constants.ConstantTable;
import com.dms.api.modules.dao.sys.user.SysUserAuditDao;
import com.dms.api.modules.service.mall.order.repo.AuditOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class AuditOrderServiceImpl implements AuditOrderService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CheckTraPwdUtils checkTraPwdUtils;

    @Autowired
    private SysUserAuditDao sysUserAuditDao;

    @Value("${sms.smsAppKey_repoAudit}")
    private String smsAppKey_repoAudit;

    @Value("${sms.channelNo_repoAudit}")
    private String channelNo_repoAudit;

    @Value("${sms.sms_switch}")
    private String sms_switch;

    //查询我审批的订单
    @Override
    public JSONObject queryList(String url, Map<String, String> reqParams) {

        logger.info("查询我审批的订单接口-请求：url: {} parmas: {}", url, reqParams);

        logger.info("开始调接口");
        //开始调接口
        String response = HttpClientUtil.doGet(url, reqParams,30000);
        if (StringUtils.isBlank(response)){
            logger.info("接口请求异常");
            return null;
        }

        //响应信息
        JSONObject jsonObject = JSONObject.parseObject(response);
        String rc = jsonObject.getString("rc");
        String msg = jsonObject.getString("msg");
        if (!StringUtils.equalsIgnoreCase(rc, "200")) {
            logger.info("接口调用失败, 错误信息：{}", msg);
            return null;
        }
        logger.info("接口调用成功，返回结果：{}", jsonObject);

        return jsonObject;
    }

    @Override
    public R sendSmsCode(String url, Map <String, String> reqParams, JSONObject syncParams) {
        logger.info(">>> 开始调接口，发送短信验证码或交易密码开关");
        JSONObject jsonObject = repoTransfer(reqParams);
        //响应信息
        String rc = jsonObject.getString("rc");
        String msg = jsonObject.getString("msg");
        String exception = jsonObject.getString("exception");
        if (!StringUtils.isBlank(exception)) {
            logger.info("接口调用异常, 异常信息：{}", jsonObject.getString("message"));
            return R.error("接口调用异常");
        }
        if (!StringUtils.equalsIgnoreCase(rc, "200")) {
            if (StringUtils.equalsIgnoreCase(rc,"2")){ //重复划转,

                logger.info(">>> 【请求重复】:划转已经成功，更新状态失败，调用审核结果同步到A系统接口更新审核状态");
                JSONObject syncJsonObject = this.syncAuditStatus(syncParams);
                if (syncJsonObject != null && StringUtils.equalsIgnoreCase(syncJsonObject.getString("rc"), "200")){
                    logger.info(">>> 重新更新审核状态到A成功");
                }

                logger.info(">>> 【请求重复】:划转已经成功，更新状态失败，调用审核接口重新更新审核状态");
                String response = HttpClientUtil.doPostJson(url, JSON.toJSONString(reqParams),30000);
                JSONObject rs = JSONObject.parseObject(response);
                if (StringUtils.equalsIgnoreCase(rs.getString("rc"), "200")) {
                    logger.info(">>> 重新更新审核状态成功");
                    return R.error("订单已经完成，重更新审核状态成功");
                }
            }
            logger.info("接口调用失败, 错误信息：{}", msg);
            return R.error(msg);
        }
        logger.info(">>> 短信验证码或交易密码开关发送成功");

        return R.ok().put("data", jsonObject);
    }

    @Override
    public R check(String url, Map<String, String> reqParams, JSONObject syncParams, Map<String, String> smsParams, String loginCode) {
        logger.info("我的审批订单审批接口-请求：logincode:{} url: {} parmas: {}", loginCode, url, reqParams);

        //查询用订单对应商城用户信息
        logger.info(">>> 开始查询用订单对应商城用户余额");
        List <Map <String, Object>> maps = sysUserAuditDao.queryMallUserInfo(loginCode);

        //调用I的划账接口（支付类型为'线下支付'无需调用划账接口）
        if (StringUtils.equalsIgnoreCase("ONLINEL",reqParams.get("payTYpe"))) {

            if (StringUtils.isBlank(reqParams.get("smsStatus"))){ //确认方式：交易密码
                try {
                    logger.info(">>> 开始验证交易密码");
                    if (checkTraPwdUtils.affirmPwd(reqParams.get("traPwd"))){
                        logger.info(">>> 交易密码验证通过");
                    }else{
                        logger.info(">>> 交易密码错误或无交易密码");
                        return R.error("交易密码错误或无交易密码");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.info(">>> 交易密码验证，未知异常：{}", e);
                    return R.error("交易密码验证，未知异常");
                }
            }

            logger.info(">>> 开始调用回购划转接口");
            //移除多余参数
            reqParams.remove("traPwd");
            JSONObject rs = repoTransfer(reqParams);
            if (rs == null){
                return R.error("操作失败，资金划转失败");
            }
            if (!StringUtils.isBlank(rs.getString("exception"))) {
                logger.info("接口调用异常, 异常信息：{}", rs.getString("message"));
                return R.error("接口调用异常");
            }
            if (!StringUtils.equalsIgnoreCase(rs.getString("rc"),"200")){
                logger.info("操作失败: {}", rs.getString("msg"));
                return R.error(rs.getString("msg"));
            }
            logger.info(">>> 调用回购划转接口成功");
        }


        logger.info(">>> 开始调用审核结果同步到A系统接口");
        //审核结果同步到A系统
        JSONObject syncJsonObject = this.syncAuditStatus(syncParams);
        if (syncJsonObject == null || !StringUtils.equalsIgnoreCase(syncJsonObject.getString("rc"), "200")){
            logger.info("审核结果同步到A系统失败");
            return R.error("操作失败");
        }
        logger.info(">>> 结束调用审核结果同步到A系统接口");


        logger.info(">>> 开始调用审核接口");
        //开始调接口 ，划账成功 -> 审核（暂不考虑事务，以划账结果为准）
        logger.info(">>> 请求参数：{}", reqParams);
        String response = HttpClientUtil.doPostJson(url, JSON.toJSONString(reqParams),30000);
        if (StringUtils.isBlank(response)){
            logger.info("接口请求异常");
        }

        //响应信息
        JSONObject jsonObject = JSONObject.parseObject(response);
        String rc = jsonObject.getString("rc");
        String msg = jsonObject.getString("msg");
        String exception = jsonObject.getString("exception");
        if (!StringUtils.isBlank(exception)) {
            logger.info("接口调用异常, 异常信息：{}", jsonObject.getString("message"));
            return R.error("操作失败");
        }
        if (!StringUtils.equalsIgnoreCase(rc, "200")) {
            logger.info("接口调用失败, 错误信息：{}", msg);
            return R.error("操作失败");
        }
        logger.info("我的审批订单审批接口-响应：{}", jsonObject);


        if (StringUtils.equalsIgnoreCase(sms_switch, "true") && StringUtils.equalsIgnoreCase("ONLINEL",smsParams.get("payTYpe"))) { //开关为开&&线上划账
            logger.info("开始调用回购审批完成调用短信接口");
            JSONObject repoAuditSmsJson = this.repoAuditSms(smsParams, maps);
            if (repoAuditSmsJson == null || !StringUtils.equalsIgnoreCase(repoAuditSmsJson.getString("rc"), "200")) {
                logger.info("回购审批成功提醒短信发送失败");
            }
        }

        return R.ok();
    }

    //回购请求资金划转
    public JSONObject repoTransfer(Map<String, String> reqParams){

        JSONObject jsonObject = null;
        try{
            String ip = StringUtils.equalsIgnoreCase(reqParams.get("mall"), "KKG") ? HostsConfig.hostsI : HostsConfig.hostsIA;
            String url = ip+ ConstantTable.BACK_PAY;
            logger.info("回购请求资金划转接口: 请求 url:{}  param:{}", url, reqParams);
            String result = HttpClientUtil.doPostJson(url, JSONObject.toJSONString(reqParams), 30000);
            logger.info("回购请求资金划转接口-响应:{}", result);
            jsonObject = JSONObject.parseObject(result);
            String exception = jsonObject.getString("exception");
            if (!StringUtils.isBlank(exception)) {
                logger.info("接口调用异常, 异常信息：{}", jsonObject.getString("message"));
                return null;
            }
            if (!StringUtils.equalsIgnoreCase(jsonObject.getString("rc"), "200")) {
                logger.info("回购请求资金划转处理失败:{}", jsonObject.getString("msg"));
                return jsonObject;
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("回购请求资金划转接口异常：{}", e);
        }

        return jsonObject;
    }

    @Override
    public JSONObject getOrderDetail(String url, Map <String, String> reqParams) {
        logger.info("获取订单详情接口-请求：url: {} parmas: {}", url, reqParams);

        logger.info("开始调接口");
        //开始调接口
        String response = HttpClientUtil.doPostJson(url, JSON.toJSONString(reqParams),30000);
        if (StringUtils.isBlank(response)){
            logger.info("接口请求异常");
            throw new RuntimeException("接口请求异常");
        }

        //响应信息
        JSONObject jsonObject = JSONObject.parseObject(response);
        String rc = jsonObject.getString("rc");
        String msg = jsonObject.getString("msg");
        String exception = jsonObject.getString("exception");
        if (!StringUtils.isBlank(exception)) {
            logger.info("接口调用异常, 异常信息：{}", jsonObject.getString("message"));
            return null;
        }
        if (!StringUtils.equalsIgnoreCase(rc, "200")) {
            logger.info("接口调用失败, 错误信息：{}", msg);
            return null;
        }
        logger.info("获取订单详情接口-响应：{}", jsonObject);

        return jsonObject;
    }

    //审核结果同步到A系统
    @Override
    public JSONObject syncAuditStatus(JSONObject syncParams){

        JSONObject jsonObject = null;
        try{
            String ip = StringUtils.equalsIgnoreCase(syncParams.getString("mall"), "KKG") ? HostsConfig.hostsI : HostsConfig.hostsIA;
            String url = ip + ConstantTable.BUY_BACK_REVIEW;
            logger.info("审核结果同步到A系统接口: 请求 url:{}  param:{}", url, syncParams);
            //移除非传入参数
            syncParams.remove("mall");
            syncParams.remove("key");
            String result = HttpClientUtil.doPostJson(url, JSONObject.toJSONString(syncParams), 30000);
            logger.info("审核结果同步到A系统接口-响应:{}", result);
            jsonObject = JSONObject.parseObject(result);
            String exception = jsonObject.getString("exception");
            if (!StringUtils.isBlank(exception)) {
                logger.info("接口调用异常, 异常信息：{}", jsonObject.getString("message"));
                return null;
            }
            if (!StringUtils.equalsIgnoreCase(jsonObject.getString("rc"), "200")) {
                logger.info("审核结果同步到A系统处理失败:{}", jsonObject.getString("msg"));
                return jsonObject;
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("审核结果同步到A系统接口异常：{}", e);
        }

        return jsonObject;
    }

    //回购审批成功短信提醒
    @Override
    public JSONObject repoAuditSms(Map<String, String> smsParams, List <Map <String, Object>> maps) {

        logger.debug("查询用订单对应商城用户信息: {}", JSON.toJSONString(maps));
        if (maps == null  || maps.size() <= 0) return null;
        Map <String, Object> map = maps.get(0);
        //获得当前时间
        LocalDate localDate = LocalDate.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String time = localDate.format(dtf);//时间
        //模板参数
        String client = map.get("userName").toString();//客户姓名
        String goods = smsParams.get("productName");//商品名
        String orderid = smsParams.get("requestNo");//回购单号
        String amt = smsParams.get("amt");//回购款额
        BigDecimal amtBig = new BigDecimal(amt);
        String fund = amtBig.divide(new BigDecimal("100"),2,BigDecimal.ROUND_HALF_UP).toString();//回购款额
        String balance = map.get("balance").toString(); //到账前商城账户金额
        String newbalance = new BigDecimal(balance).add(new BigDecimal(fund)).toString(); //到账后商城账户金额 = 到账前商城账户金额 + 回购款额

        JSONObject reqAuditSmsParams = new JSONObject();
        reqAuditSmsParams.put("channelNo",channelNo_repoAudit);//短信模板号【配置】
        reqAuditSmsParams.put("sign",SmsConfig.smsSign);//短信接口盐值【配置】
        reqAuditSmsParams.put("applicationKey",smsAppKey_repoAudit);//应用秘钥【配置】
        reqAuditSmsParams.put("phone",map.get("phone"));//手机号
        reqAuditSmsParams.put("templateType","1");
        reqAuditSmsParams.put("isTiming", 0);
        reqAuditSmsParams.put("definiteTime", "0");
        reqAuditSmsParams.put("messageType", "0");
        reqAuditSmsParams.put("validTime", 5);
        reqAuditSmsParams.put("params", "{'client':'"+client+"',"+"'time':'"+time+"',"+"'goods':'"+goods+"',"+"'orderid':'"+orderid
                                +"',"+"'fund':'"+fund+"',"+"'balance':'"+balance+"',"+"'newbalance':'"+newbalance+"'}");
        logger.info("回购审批完成调用短信模板的完整参数:{}",reqAuditSmsParams);

        JSONObject jsonObject = null;
        try {
            String url = SmsConfig.smsIp + ConstantTable.SEND_SMS;
            logger.info("回购审批成功短信提醒接口，请求url:{}, 请求参数reqParams:{}",url,reqAuditSmsParams);
            String result = HttpClientUtil.doPostJson(url, JSON.toJSONString(reqAuditSmsParams), 30000);
            logger.info("回购审批成功短信提醒接口-响应:{}", result);

            jsonObject = JSONObject.parseObject(result);
            String exception = jsonObject.getString("exception");
            if (!StringUtils.isBlank(exception)) {
                logger.info("接口调用异常, 异常信息：{}", jsonObject.getString("message"));
                return null;
            }
            if (!StringUtils.equalsIgnoreCase(jsonObject.getString("rc"), "200")) {
                logger.info("调用回购审批成功短信提醒接口失败:{}", jsonObject.getString("msg"));
                return jsonObject;
            }

        } catch (Exception e){
            e.printStackTrace();
            logger.error("回购审批成功短信提醒接口接口异常：{}", e);
        }

        return jsonObject;
    }

}

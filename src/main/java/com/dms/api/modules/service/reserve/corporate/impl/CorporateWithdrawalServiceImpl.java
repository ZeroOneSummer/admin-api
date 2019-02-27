package com.dms.api.modules.service.reserve.corporate.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dms.api.common.utils.HttpClientUtil;
import com.dms.api.common.utils.R;
import com.dms.api.common.utils.StringUtils;
import com.dms.api.core.config.HostsConfig;
import com.dms.api.core.constants.ConstantTable;
import com.dms.api.modules.service.reserve.corporate.CorporateWithdrawalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author: hushuangxi
 * @CreateDate: 2018/9/26 11:41
 * @Description: 作用描述
 */
@Service
public class CorporateWithdrawalServiceImpl implements CorporateWithdrawalService {

    Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 服务商的预申请对公提现，先调I服务的预申请对公提现，再调审批服务的对公提现申请
     * @param url   I服务的预申请对公提现url
     * @param reqParams  I服务的预申请对公提现参数
     * @param aduitParams 审批服务的对公提现申请参数
     * @return
     */
    @Override
    public R getwithdrawal(String url, Map<String, String> reqParams, JSONObject aduitParams) {
        logger.info("预申请对公提现接口请求：url: {}， reqParams: {}，aduitParams: {}",url,JSON.toJSONString(reqParams),aduitParams);
        logger.info("开始调用预申请对公提现接口");
        String response = HttpClientUtil.doPostJson(url, JSON.toJSONString(reqParams),30000);
        //调用I服务的响应信息
        JSONObject jsonObject = JSONObject.parseObject(response);
        if (StringUtils.isBlank(response)){
            logger.info("预申请对公提现接口请求异常");
            return R.error("预申请对公提现接口请求异常");
        }
        if (!StringUtils.equalsIgnoreCase(jsonObject.getString("rc"),"200")){
            logger.info("调用接口失败，错误信息：{}", jsonObject.getString("msg"));
            return R.error(jsonObject.getString("msg"));
        }
        logger.info("调用I服务预申请对公提现的接口结束，接口响应：{}", jsonObject);
        JSONObject data = jsonObject.getJSONObject("data");
        String withdrawSerialOrderNo = (String) data.get("withdrawSerialOrderNo");
        reqParams.put("withdrawSerialOrderNo",withdrawSerialOrderNo);
        //审批服务的url
        String aduitUrl = HostsConfig.hostsAudit + ConstantTable.ADD_VAC;
        //移除相关参数
        reqParams.remove("channelType");
        reqParams.remove("accountType");
        reqParams.put("userName",(String) aduitParams.get("userName"));
        reqParams.put("applyTime",String.valueOf(System.currentTimeMillis()));
        aduitParams.put("vaMap", reqParams);

        logger.info("审批服务的对公提现申请：url: {} aduitParams: {}",aduitUrl,JSON.toJSONString(aduitParams));
        logger.info("开始调用审批服务的预申请对公提现审批接口");
        String result = HttpClientUtil.doPostJson(aduitUrl, JSON.toJSONString(aduitParams),30000);
        logger.info("预申请对公提现审批请求：aduitParams: {}",aduitParams);
        //调用审批服务的响应信息
        JSONObject auditResult = JSONObject.parseObject(result);
        String msg = auditResult.getString("msg");
        if (!StringUtils.equalsIgnoreCase(auditResult.getString("rc"), "200")) {
            logger.info("接口调用失败：msg:{}", msg);
            return R.error(msg);
        }
        logger.info("调用预申请对公提现接口结束：{}",auditResult);

        return R.ok().put("data", auditResult);
    }

    /**
     * 查询我审批的订单
     * @param reqParams
     * @return
     */
    @Override
    public JSONObject queryAuditList(String url,Map<String, String> reqParams) {
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

    /**
     * 对公提现审批驳回,先调I服务的对公提现驳回，再调审批服务的驳回
     * @param url
     * @param reqParams    I服务参数
     * @param aduitParams  审批服务参数
     * @return
     */
    @Override
    public R rejectwithdrawal(String url, Map<String, String> reqParams, Map<String, String> aduitParams) {
        logger.info("对公提现驳回接口请求：url: {}, reqParams: {}, aduitParams: {}",url,JSON.toJSONString(reqParams),JSON.toJSONString(aduitParams));
        logger.info("开始调用I服务的对公提现驳回接口");
        String response = HttpClientUtil.doPostJson(url, JSON.toJSONString(reqParams),30000);
        JSONObject jsonObject = JSONObject.parseObject(response);
        String msg = jsonObject.getString("msg");
        if (!StringUtils.equalsIgnoreCase(jsonObject.getString("rc"), "200")) {
            logger.info("接口调用失败：msg:{}", jsonObject.getString("msg"));
            return R.error(msg);
        }
        logger.info("调用I服务的对公提现驳回接口结束，接口响应：{}",jsonObject);

        String aduitUrl = HostsConfig.hostsAudit + ConstantTable.PASS_MYAUDIT;
        aduitParams.remove("traPwd");
        logger.info("对公提现的审批服务请求：aduitUrl: {} aduitParams: {}",url,JSON.toJSONString(aduitParams));
        logger.info("开始调用审批服务的审批接口");
        String result = HttpClientUtil.doPostJson(aduitUrl, JSON.toJSONString(aduitParams),30000);
        //调用审批服务的响应信息
        JSONObject auditResult = JSONObject.parseObject(result);
        if (!StringUtils.equalsIgnoreCase(auditResult.getString("rc"), "200")) {
            logger.info("调用调用失败：msg:{}", auditResult.getString("msg"));
            return R.error(auditResult.getString("msg"));
        }
        logger.info("调用审批服务的审批接口结束，接口响应：{}",auditResult);
        return R.ok().put("data", auditResult);
    }

    /**
     * 渠道专员和出纳审批同意,只调用审批服务
     * @param url  审批服务url
     * @param auditReqParams 审批服务参数
     * @return
     */
    @Override
    public R getPassMyAudit(String url, Map<String, String> auditReqParams) {
        logger.info("渠道专员和出纳的审批提现请求：url: {} auditReqParams: {}",url,JSON.toJSONString(auditReqParams));
        logger.info("开始调用审批服务的审批提现接口");
        String response = HttpClientUtil.doPostJson(url, JSON.toJSONString(auditReqParams),30000);
        JSONObject jsonObject = JSONObject.parseObject(response);
        String msg = jsonObject.getString("msg");
        if (!StringUtils.equalsIgnoreCase(jsonObject.getString("rc"), "200")) {
            logger.info("接口调用失败：msg:{}", msg);
            return R.error(msg);
        }
        logger.info("审批角色为渠道专员和出纳的审批提现接口结束：{}",jsonObject);

        return R.ok().put("data", jsonObject);
    }

    /**
     * 财务经理审批同意，先调用审批服务，再调用I服务的确认对公打款
     * @param url 审批服务url
     * @param auditReqParams  审批服务参数
     * @param reqParams   I服务的确认对公打款参数
     * @return
     */
    @Override
    public R getPassMyAuditAndPay(String url, Map<String, String> auditReqParams, Map<String, String> reqParams) {
        logger.info("财务经理审批提现请求：url: {} auditReqParams: {},reqParams: {}",url,JSON.toJSONString(auditReqParams),JSON.toJSONString(reqParams));
        logger.info("开始调用审批服务的审批提现接口");
        String response = HttpClientUtil.doPostJson(url, JSON.toJSONString(auditReqParams),30000);
        JSONObject jsonObject = JSONObject.parseObject(response);
        String msg = jsonObject.getString("msg");
        if (!StringUtils.equalsIgnoreCase(jsonObject.getString("rc"), "200")) {
            logger.info("接口调用失败：msg:{}", msg);
            return R.error(msg);
        }
        logger.info("财务经理审批提现请求审批提现接口结束：{}",jsonObject);

        //I服务的对公打款url
        String confirmUrl = HostsConfig.hostsI + ConstantTable.WITHDRAW_ACK;
        logger.info("I服务的对公打款请求：confirmUrl: {} reqParams: {}",confirmUrl,JSON.toJSONString(reqParams));
        logger.info("开始调用I服务的对公打款接口");

        String result = HttpClientUtil.doPostJson(confirmUrl, JSON.toJSONString(reqParams),30000);
        //调用审批服务的响应信息
        JSONObject confirmResult = JSONObject.parseObject(result);
        if (!StringUtils.equalsIgnoreCase(confirmResult.getString("rc"), "200")) {
            logger.info("接口调用失败：msg:{}", confirmResult.getString("msg"));
            return R.error(confirmResult.getString("msg"));
        }
        logger.info("调用预申请对公提现接口结束：{}",confirmResult);

        return R.ok().put("data", confirmResult);
    }

}

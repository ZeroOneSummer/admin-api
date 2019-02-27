package com.dms.api.modules.service.reserve.corporate.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dms.api.common.utils.HttpClientUtil;
import com.dms.api.common.utils.R;
import com.dms.api.common.utils.StringUtils;
import com.dms.api.modules.dao.reserve.corporate.CorporateBankCardDao;
import com.dms.api.modules.service.reserve.corporate.CorporateBankCardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author: hushuangxi
 * @CreateDate: 2018/9/26 11:41
 * @Description: 作用描述
 */
@Service
public class CorporateBankCardServiceImpl implements CorporateBankCardService {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CorporateBankCardDao corporateBankCardDao;

    @Override
    public List<Map<String, String>> getOrgCodes(Long orgId) {
        return corporateBankCardDao.getOrgCodes(orgId);
    }

    /**
     * 调用I服务获取服务商在B系统的可出资金
     * @return
     */
    @Override
    public JSONObject getAllowWithdrawal(String url,Map<String,String> reqParams) {
        logger.info("调用I服务获取服务商在B系统的可出资金接口-请求：url: {},params: {}",url,JSON.toJSONString(reqParams));
        logger.info(">>开始调用接口");
        String response = HttpClientUtil.doPostJson(url,JSON.toJSONString(reqParams),30000);
        if (StringUtils.isBlank(response)){
            logger.info("调用I服务获取服务商在B系统的可出资金接口请求异常");
            return null;
        }
        //响应信息
        JSONObject jsonObject = JSONObject.parseObject(response);
        if (!StringUtils.equalsIgnoreCase(jsonObject.getString("rc"),"200")){
            logger.info("调用接口失败，错误信息：{}", jsonObject.getString("msg"));
            return null;
        }
        logger.info("调用接口结束，接口响应：{}", jsonObject);

        return jsonObject;
    }

    //对公绑卡
    @Override
    public R BindBankCard(String url, Map<String, String> reqParams) {
        logger.info("对公绑定银行卡接口-请求：url: {} reqParams: {}",url,JSON.toJSONString(reqParams));

        String response = HttpClientUtil.doPostJson(url,JSON.toJSONString(reqParams),30000);

        JSONObject jsonObject = JSONObject.parseObject(response);
        String msg = jsonObject.getString("msg");
        String exception = jsonObject.getString("exception");
        if (!StringUtils.isBlank(exception)) {
            logger.info("接口调用异常, 异常信息：{}", jsonObject.getString("message"));
            return R.error("接口调用异常");
        }
        if (!StringUtils.equalsIgnoreCase(jsonObject.getString("rc"), "200")) {
            logger.info("接口调用失败：msg:{}", msg);
            return R.error(msg);
        }

        logger.info("对公绑定银行卡接口-响应:{}", response);

        return R.ok().put("data", jsonObject);
    }

    //对公解绑银行卡
    @Override
    public R UntieBankCard(String url, Map<String, String> reqParams) {
        logger.info("对公解绑银行卡接口请求: url: {} reqParams {}",url,JSON.toJSONString(reqParams));

        String response = HttpClientUtil.doPostJson(url,JSON.toJSONString(reqParams),30000);
        if (StringUtils.isBlank(response)){
            logger.info("请求接口异常,无返回值");
            throw new RuntimeException("接口请求异常");
        }
        //响应信息
        JSONObject jsonObject = JSON.parseObject(response);
        String msg = jsonObject.getString("msg");
        String exception = jsonObject.getString("exception");
        if (!StringUtils.isBlank(exception)) {
            logger.info("接口调用异常, 异常信息：{}", jsonObject.getString("message"));
            return R.error("接口调用异常");
        }
        if (!StringUtils.equalsIgnoreCase(jsonObject.getString("rc"),"200")){
            logger.info("接口调用失败：msg:{}",msg);
            return R.error(msg);

        }
        logger.info("接口调用结束，接口响应：{}", jsonObject);
        return R.ok().put("data",jsonObject);
    }

    //获取银行卡编码列表
    @Override
    public List<Map<String, String>> getBankCodes() {
        return corporateBankCardDao.getBankCodes();
    }

    //查询对公银行卡信息
    @Override
    public JSONObject queryList(String url, Map<String, String> reqParams) {
        logger.info("查询对公银行卡信息的接口-入参: url: {} reqParams: {}", url, JSON.toJSONString(reqParams));

        logger.info("开始调用接口");
        //开始调接口
        String response = HttpClientUtil.doPostJson(url, JSON.toJSONString(reqParams) ,30000);
        if (StringUtils.isBlank(response)){
            logger.info("查询对公银行卡信息的接口请求异常");
            return null;
        }

        //响应信息
        JSONObject jsonObject = JSONObject.parseObject(response);
        if (!StringUtils.equalsIgnoreCase(jsonObject.getString("rc"),"200")){
            logger.info("调用接口失败，错误信息：{}", jsonObject.getString("msg"));
            return null;
        }
        logger.info("调用接口结束，接口响应：{}", jsonObject);

        return jsonObject;
    }

}

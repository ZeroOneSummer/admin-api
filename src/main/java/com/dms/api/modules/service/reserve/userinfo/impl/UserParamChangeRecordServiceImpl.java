package com.dms.api.modules.service.reserve.userinfo.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dms.api.common.utils.HttpClientUtil;
import com.dms.api.common.utils.StringUtils;
import com.dms.api.modules.service.reserve.userinfo.UserParamChangeRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Administrator
 * @Description: TODO
 * @date
 */
@Service
public class UserParamChangeRecordServiceImpl implements UserParamChangeRecordService {
    private static Logger logger = LoggerFactory.getLogger(UserParamChangeRecordServiceImpl.class);

    @Override
    public JSONObject getUserParamChangeRecordList(String url, Map<String, Object> reqParams) {
        logger.info("获取用户修改风控记录-请求：url: {} parmas: {}", url, reqParams);

        logger.info("开始调接口");
        //开始调接口
        String response = HttpClientUtil.doPostJson(url, JSON.toJSONString(reqParams),30000);
        if (StringUtils.isBlank(response)){
            logger.info("接口请求异常: 响应为空");
            return null;
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
        logger.info("获取用户修改风控记录接口-响应：{}", jsonObject);

        return jsonObject;
    }
}

package com.dms.api.core.https;

import com.alibaba.fastjson.JSONObject;
import com.dms.api.common.exception.DMException;
import com.dms.api.core.https.base.HttpBase;
import com.dms.api.core.https.bean.HttpResult;
import com.dms.api.core.logs.LogHttpHandler;
import com.dms.api.core.logs.LogType;

import java.util.Map;

/**
 * HTTP接口调用统一处理
 */
public class HttpISYSClient extends HttpBase {

    /**
     * I接口调用统一入口
     * @param url   请求路径
     * @param headers   请求头
     * @param params	请求参数（jsonString）
     * @return 	请求后结果
     */
    public static HttpResult postHttpResult(final String url, final Map<String, ?> headers, final Map<String, ?> params) throws Exception {
        return postHttpResult(url, headers, params, LOG_PRINT);
    }

    /**
     * I接口调用统一入口
     * @param url   请求路径
     * @param headers   请求头
     * @param params	请求参数（jsonString）
     * @return 	请求后结果
     */
    public static HttpResult postHttpResult(final String url, final Map<String, ?> headers, final Map<String, ?> params, Boolean printLog) throws Exception {
        String res = null;
        long start = System.currentTimeMillis();
        try {
            res = doPost(url, headers, params);
            HttpResult result = JSONObject.parseObject(res, HttpResult.class);
            if (result == null || result.fail()) {
                throw new DMException(HttpResult.REQUEST_EXCEPTION, 500);
            }

            if(!result.success()){
                LogHttpHandler.logSysInfo(LogType.W, url, params, res,start);
            }
            else if(printLog){
                LogHttpHandler.logSysInfo(LogType.I, url, params, res,start);
            }
            return result;
        } catch (Exception e) {
            LogHttpHandler.logSysInfo(LogType.E, url, params, res + "|" + e.getMessage(),start);
            throw new Exception("ERROR:" + e.getMessage());
        }
    }
}

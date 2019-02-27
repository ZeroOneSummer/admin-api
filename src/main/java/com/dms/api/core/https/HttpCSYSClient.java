package com.dms.api.core.https;

import com.dms.api.core.https.base.HttpBase;
import com.dms.api.core.logs.LogHttpHandler;
import com.dms.api.core.logs.LogType;

import java.util.Map;

/**
 * C-SYS
 */
public class HttpCSYSClient extends HttpBase {
    /**
     * C系统接口调用统一入口
     * */
    public static String postCSysString(final String url, Map<String, String> params) throws Exception {
        String res = null;
        long start = System.currentTimeMillis();
        try {
            res = doPostC(url, params);
            LogHttpHandler.logSysInfo(LogType.I, url, params, res,start);
            return res;
        } catch (Exception e) {
            LogHttpHandler.logSysInfo(LogType.E, url, params, res + "|" + e.getMessage(),start);
            throw new Exception("ERROR:" + e.getMessage());
        }
    }

    /**
     * C系统接口调用统一入口
     * */
    public static String getCSysString(final String url, final Map<String, ?> headers, Map<String, ?> params) throws Exception {
        String res = null;
        long start = System.currentTimeMillis();
        try {
            res = doGet(url, headers, params);
            LogHttpHandler.logSysInfo(LogType.I, url, params, res,start);
            return res;
        } catch (Exception e) {
            LogHttpHandler.logSysInfo(LogType.E, url, params, res + "|" + e.getMessage(),start);
            throw new Exception("ERROR:" + e.getMessage());
        }
    }
}

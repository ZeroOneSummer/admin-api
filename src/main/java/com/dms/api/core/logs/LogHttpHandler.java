package com.dms.api.core.logs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Http logs
 */
public class LogHttpHandler extends LogHandler {

    private static final Logger logger = LoggerFactory.getLogger(LogHttpHandler.class);

    public static void logSysInfo(LogType type, String url, Map<String, ?> params, String result, long start){
        String msgStr=url+SPL+params+SPL+result+SPL+SPL+(System.currentTimeMillis() - start) + "ms";
        logPrint(type, msgStr);
    }

    public static void logSysInfo(LogType type, String url, Map<String, ?> headers, Map<String, ?> params, String result, long start){
        String msgStr=url+SPL+headers+SPL+params+SPL+result+SPL+SPL+(System.currentTimeMillis() - start) + "ms";
        logPrint(type, msgStr);
    }

    public static void logSysInfo(LogType type,String url,String params,String result,long start) {
        String msgStr =url+SPL+params+SPL+result+SPL+SPL+(System.currentTimeMillis()-start)+"ms";
        logPrint(type, msgStr);
    }

    /**
     * 根据不同类型打印不同日志
     * @param type 类型
     * @param content 内容
     */
    private static void logPrint(LogType type, String content) {
        content = entryPassword(content);
        if(type.equals(LogType.D)){
            logger.debug(content);
        }else if(type.equals(LogType.W)){
            logger.warn(content);
        }else if(type.equals(LogType.E) || type.equals(LogType.ALL)){
            logger.error(content);
        }else {
            logger.info(content);
        }
    }
}

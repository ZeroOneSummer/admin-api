package com.dms.api.core.https.bean;

import com.dms.api.common.utils.IPUtils;

import java.util.HashMap;
import java.util.UUID;

/**
 * Request 请求头对象
 */
public class HttpHeaders extends HashMap{
    public static final String ISYS_TONKEN = "token";
    public static final String ISYS_REQUEST_IP = "ip";
    public static final String ISYS_REQUEST_CLIENT = "client";
    public static final String ISYS_REQUEST_REQ_FLAG = "reqFlag";
    public static final String ISYS_REQUEST_REQ_TIME = "reqTime";
    public static final String ISYS_REQUEST_DEALER_ID = "dealerId";
    public static final String ISYS_REQUEST_CONTENT_TYPE = "Content-Type";
    public static final String ISYS_REQUEST_SERVICE_NAME = "serviceName";
    /**
     * 默认值
     */
    public static final String ISYS_REQUEST_IP_DEFAULT_VALUE = "127.0.0.1";
    public static final String ISYS_REQUEST_DEALER_ID_DEFAULT_VALUE = "123456";
    public static final String ISYS_REQUEST_CONTENT_TYPE_DEFAULT_VALUE = "application/json";
    public static final String ISYS_REQUEST_SERVICE_NAME_DEFAULT_VALUE = "nil";
    public static final String ISYS_REQUEST_CLIENT_DEFAULT_VALUE = "nil";


    public static HttpHeaders getHeaders(){
        //TODO return new HttpHeaders(UserManager.getToken());
        return new HttpHeaders("");
    }

    public static HttpHeaders getHeaders(String token){
        return new HttpHeaders(token);
    }

    public HttpHeaders(){
        this.init();
    }

    public HttpHeaders(String token){
        this.init();
        this.put(ISYS_TONKEN, token);
    }

    private void init(){
        this.clear();
        String ip = IPUtils.getIp();
        if(null != ip && !"".equals(ip)) {
            this.put(ISYS_REQUEST_IP,ip);
        }else {
            this.put(ISYS_REQUEST_IP, ISYS_REQUEST_IP_DEFAULT_VALUE);
        }
        this.put(ISYS_REQUEST_REQ_FLAG, UUID.randomUUID().toString());
        this.put(ISYS_REQUEST_REQ_TIME, System.currentTimeMillis() + "");
        this.put(ISYS_REQUEST_DEALER_ID, ISYS_REQUEST_DEALER_ID_DEFAULT_VALUE);
        this.put(ISYS_REQUEST_CLIENT, ISYS_REQUEST_CLIENT_DEFAULT_VALUE);
        this.put(ISYS_REQUEST_CONTENT_TYPE, ISYS_REQUEST_CONTENT_TYPE_DEFAULT_VALUE);
        this.put(ISYS_REQUEST_SERVICE_NAME, ISYS_REQUEST_SERVICE_NAME_DEFAULT_VALUE);
    }
}

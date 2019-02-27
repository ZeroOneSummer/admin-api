package com.dms.api.core.https.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * I接口服务返回结果
 */
@Data
public class HttpResult implements Serializable {
    public final static Integer CODE_SUCCESS = 200; // 成功
    public final static Integer CODE_ERROR = 400; // ERROR
    public final static Integer CODE_LOGINOUT = -108; //B系统退出登录
    public final static Integer CODE_TOKENUN = -402; //token失效，请重新登录
    public final static Integer STATUS_ORDER_FALL = -8201;  //优惠券使用成功，下单失败
    public final static String LOGIN_OUT = "系统登录状态失效，请重新登录";
    public final static String REQUEST_EXCEPTION = "网络异常，请求失败";
    // 返回码
    private Integer rc;
    // 描述信息
    private String msg;
    // 数据
    private String data;

    //使用优惠券状态码
    private Integer status;

    public HttpResult(){}

    public HttpResult(Integer rc, String msg){
        this.rc = rc;
        this.msg = msg;
    }

    /*
    * 验证请求结果是否出错
    * */
    public boolean fail(){
        return this.getRc() == null;
    }

    /*
    * 验证请求结果是否成功
    * */
    public boolean success(){
        return CODE_SUCCESS.equals(this.getRc());
    }

    /*
    * 验证请求登录状态是否正常
    * */
    public boolean logined(){
        return !CODE_LOGINOUT.equals(this.getRc()) && !CODE_TOKENUN.equals(this.getRc());
    }

    public static HttpResult failResult(String error){
        if(error == null || "".equals(error)){
            return new HttpResult(CODE_ERROR, "系统繁忙");
        }
        return new HttpResult(CODE_ERROR, error);
    }
}

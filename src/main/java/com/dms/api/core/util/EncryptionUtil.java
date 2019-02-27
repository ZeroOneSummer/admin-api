package com.dms.api.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @version V1.0
 * @Description:
 * @author:dustine
 * @date 2017/10/24 下午1:47
 */
public class EncryptionUtil {
    private final static Logger logger= LoggerFactory.getLogger(EncryptionUtil.class);

    /**
     * 获取参数签名
     * @param paramMap  签名参数
     * @param secretKey 签名密钥
     * @param keyStorePath  私钥证书
     * @return
     */

    //将非空参数按照接收的顺序进行验证  然后加上密码证书加密
    public static String  getSign (Map<String , String> paramMap , String secretKey,String keyStorePath){
        SortedMap<String, Object> smap = new TreeMap<String, Object>(paramMap);
        StringBuffer stringBuffer = new StringBuffer();
        for (Map.Entry<String, Object> m : smap.entrySet()) {
            Object value = m.getValue();
            if (value != null && isNotBlank(String.valueOf(value))){
                stringBuffer.append(m.getKey()).append("=").append(value).append("&");
            }
        }
        stringBuffer.delete(stringBuffer.length() - 1, stringBuffer.length());
        String argPreSign = stringBuffer.append("&paySecret=").append(secretKey).toString();
        //logger.info("========"+argPreSign);
        String signMd5Str = MD5Util.encode(argPreSign).toUpperCase();
        //logger.info("密码"+secretKey +"证书路径"+keyStorePath);
        //要是验证加密失败直接返回空
        try {

        return RsaCodingUtil.encryptByPriPfxFile(Base64Encode(signMd5Str), keyStorePath, secretKey);

        }catch (Exception e){

            logger.info("证书加密失败,网关程序有错误"+e.getMessage());
            return "";
        }


    }

    /**
     * 获取参数拼接串
     * @param paramMap
     * @return
     */
    public static String  getParamStr(Map<String , Object> paramMap){
        SortedMap<String, Object> smap = new TreeMap<String, Object>(paramMap);
        StringBuffer stringBuffer = new StringBuffer();
        for (Map.Entry<String, Object> m : smap.entrySet()) {
            Object value = m.getValue();
            if (value != null && isNotBlank(String.valueOf(value))){
                stringBuffer.append(m.getKey()).append("=").append(value).append("&");
            }
        }
        stringBuffer.delete(stringBuffer.length() - 1, stringBuffer.length());

        return stringBuffer.toString();
    }

    /**
     * 验证商户签名
     * @param paramMap  签名参数
     * @param paySecret 签名私钥
     * @param signStr   原始签名密文
     * @return
     */
    public static boolean isRightSign(Map<String , String> paramMap , String paySecret ,String signStr,String keyStorePath){

        if (isBlank(signStr)){
            return false;
        }

        String sign = getSign(paramMap, paySecret,keyStorePath);
        if(signStr.equals(sign)){
            return true;
        }else{
            return false;
        }
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static boolean isBlank(String str) {
        return null == str || "".equals(str);
    }

    public static String Base64Encode(String str) throws Exception {

        byte[] b=str.getBytes("UTF-8");
        org.apache.commons.codec.binary.Base64 base64=new org.apache.commons.codec.binary.Base64();
        b=base64.encode(b);
        String s=new String(b);
        return s;
    }

    /**
     * 解密
     */
    public static String Base64Decode(String str) throws Exception {
//		str = str.replaceAll(" ", "+");
        return new String(new org.apache.commons.codec.binary.Base64().decode(str), "UTF-8");
    }

}

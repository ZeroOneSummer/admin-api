package com.dms.api.core.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class SmsConfig {

    public static String smsIp;

    public static String smsAppKey;

    public static String channelNo;

    public static String smsSign;

    @Value("${sms.ip}")
    public void setSmsIp(String smsIp) {
        SmsConfig.smsIp = smsIp;
    }

    @Value("${sms.smsAppKey}")
    public void setSmsAppKey(String smsAppKey) {
        SmsConfig.smsAppKey = smsAppKey;
    }

    @Value("${sms.channelNo}")
    public void setChannelNo(String channelNo) {
        SmsConfig.channelNo = channelNo;
    }

    @Value("${sms.sign}")
    public void setSmsSign(String smsSign) {
        SmsConfig.smsSign = smsSign;
    }

}

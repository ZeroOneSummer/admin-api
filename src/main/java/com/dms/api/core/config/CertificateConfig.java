package com.dms.api.core.config;

import com.dms.api.common.utils.ParamMapUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CertificateConfig {

    public static String targetAccId;

    public static String sourceAccId;

    public static String beanName;

    @Value("${collectionAccount.targetAccId}")
    public void setTargetAccId(String targetAccId) {
        this.targetAccId = targetAccId;
    }

    @Value("${collectionAccount.sourceAccId}")
    public void setSourceAccId(String sourceAccId) {
        this.sourceAccId = sourceAccId;
    }

    @Value("${mall.code}")
    public void setBeanName(String code) {
        this.beanName = ParamMapUtils.beanTypeMap.get(code);
    }

}

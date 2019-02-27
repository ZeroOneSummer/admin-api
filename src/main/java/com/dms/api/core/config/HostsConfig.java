package com.dms.api.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 接口IP统一配置类
 */
@Configuration
public class HostsConfig {

    public static String hostsA;            //A系统
    public static String hostsB;            //B系统
    public static String hostsI;            //I系统
    public static String hostsIA;           //I系统kka
    public static String hostsAudit;        //审批服务
    public static String hostsUserLevel;    //用户等级服务

    @Value("${hosts.a-sys}")
    public void setHostsA(String hostsA) {
        this.hostsA = hostsA;
    }

    @Value("${hosts.b-sys}")
    public void setHostsB(String hostsB) {
        HostsConfig.hostsB = hostsB;
    }

    @Value("${hosts.i-sys}")
    public void setHostsI(String hostsI) {
        this.hostsI = hostsI;
    }

    @Value("${hosts.ia-sys}")
    public void setHostsIA(String hostsIA) {
        this.hostsIA = hostsIA;
    }

    @Value("${hosts.user-level}")
    public void setHostsUserLevel(String hostsUserLevel) {
        HostsConfig.hostsUserLevel = hostsUserLevel;
    }

    @Value("${hosts.audit-sys}")
    public void sethostsRepoAudit(String hostsAudit) {
        this.hostsAudit = hostsAudit;
    }
}

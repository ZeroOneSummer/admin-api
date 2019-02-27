package com.dms.api.common.utils;

import com.dms.api.core.constants.ConstantTable;

/**
 * Redis所有Keys
 *
 */
public class RedisKeys {

    /**
     * 生成存储缓存的KEY，设置与获取的KEY都通过该方法获取
     * @param key
     * @return
     */
    public static String getSysConfigKey(String key){
        return ConstantTable.GLOBO_REDIS_KEY_PRIXX + key.toUpperCase();
    }


    /**
     * 用户当前等级信息
     */
    public static final String RDS_USERLEVEL_CURLEVELINFO="ADMIN:USERLEVEL:CURLEVELINFO:";
    /**
     * 系统中所有等级信息
     */
    public static final String RDS_USERLEVEL_ALLLEVELINFO="ADMIN:USERLEVEL:ALLLEVELINFO";
}

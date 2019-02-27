package com.dms.api.common.redis;

import com.alibaba.fastjson.JSONObject;
import com.dms.api.common.utils.RedisUtils;
import com.dms.api.common.utils.RedisKeys;
import com.dms.api.common.utils.RedisService;
import com.dms.api.modules.entity.sys.config.SysConfigEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 系统配置Redis
 *
 */
@Component
public class SysConfigRedis {
    @Autowired
    private RedisUtils redisUtils;
    
    @Autowired
    private RedisService redisService;

    public void saveOrUpdate(SysConfigEntity config) {
        if(config == null){
            return ;
        }
        String key = RedisKeys.getSysConfigKey(config.getKey());
        redisService.noTimeset(key, (JSONObject) JSONObject.toJSON(config));
    
    }

    public void delete(String configKey) {
        String key = RedisKeys.getSysConfigKey(configKey);
        redisService.del(key);
    }

    public SysConfigEntity get(String configKey){
        String key = RedisKeys.getSysConfigKey(configKey);
        JSONObject json=redisService.get(key, JSONObject.class);
        return JSONObject.toJavaObject(json, SysConfigEntity.class);
    }
}

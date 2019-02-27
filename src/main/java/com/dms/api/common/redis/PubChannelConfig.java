package com.dms.api.common.redis;

import com.dms.api.core.constants.ConstantTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class PubChannelConfig {
    @Autowired
    @Qualifier("noSerializerRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 发布配置信息。需要配置信息的客户端，订阅"ConstantTable.CONFIG_CHANNEL + '*' "即可。
     *
     * @param value 更新后的KEY配置。可以通过解析该值获取相应的缓存值
     * <br>格式：{父级key}-{子级key}</br>
     * <br>例如：</br>
     * <br>pp-kk  => 则可以获取key为pp，field为kk的map缓存值</br>
     */
    public void pubConfig( String value){
        redisTemplate.convertAndSend(ConstantTable.CONFIG_CHANNEL,value);
    }

}

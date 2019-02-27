package com.dms.api.common.redis;

import com.dms.api.common.utils.RedisService;
import com.dms.api.modules.entity.sys.config.SysConfigEntity;
import com.dms.api.core.constants.ConstantTable;
import com.dms.api.modules.service.sys.config.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author
 * 所有redis缓存初始化，每个方法代表一种配置缓存
 */
@Component
public class RedisInitService {

    @Autowired
    RedisService redisService;

    @Autowired
    SysConfigService sysConfigService;

    /**
     * 初始化配置信息到缓存
     */
    public void initConfig (){
        //1、获取最新的配置
        List<SysConfigEntity> sysConfigEntities = sysConfigService.queryList(null);
        Map<Long ,SysConfigEntity> cache = new HashMap<>(sysConfigEntities.size());
        for (SysConfigEntity entity:sysConfigEntities) {
            cache.put(entity.getId(),entity) ;
        }

        //2、设置最新配置到对应的缓存
        for (SysConfigEntity entity:sysConfigEntities) {
            String pkey = ConstantTable.CONFIG_CACHE_KEY_COMOMN;
            Long pid = entity.getPid();
            if ( -1L != pid){
                //父级配置
                SysConfigEntity sysConfigEntity = cache.get(pid);
                pkey = sysConfigEntity.getKey();
            }

            //放缓存
            String key = entity.getKey();
            String value = entity.getValue();
            Map<String ,String > cachemap = new HashMap<>(1);
            cachemap.put(key,value);

            redisService.setMap(pkey,cachemap);
        }
    }
}

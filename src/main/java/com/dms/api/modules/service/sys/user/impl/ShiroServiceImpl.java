package com.dms.api.modules.service.sys.user.impl;

import com.dms.api.modules.entity.sys.menu.SysMenuEntity;
import com.dms.api.core.constants.ConstantTable;
import com.dms.api.common.utils.RedisService;
import com.dms.api.modules.dao.sys.menu.SysMenuDao;
import com.dms.api.modules.dao.sys.user.SysUserDao;
import com.dms.api.modules.dao.sys.user.SysUserTokenDao;
import com.dms.api.modules.entity.sys.user.SysUserEntity;
import com.dms.api.modules.entity.sys.user.SysUserTokenEntity;
import com.dms.api.modules.service.sys.user.ShiroService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ShiroServiceImpl implements ShiroService {

    private String userInfoCKey = ConstantTable.USER_INFO_REDIS_KEY_PREFIX;

    @Autowired
    private SysMenuDao sysMenuDao;

    @Autowired
    private SysUserDao sysUserDao;

    @Autowired
    private SysUserTokenDao sysUserTokenDao;

    @Autowired
    private RedisService redisService;

    @Override
    public Set<String> getUserPermissions(SysUserEntity user) {
        List<String> permsList;

        //系统管理员，拥有最高权限
        if(user.isSysSuperAdmin()){
            List<SysMenuEntity> menuList = sysMenuDao.queryList(new HashMap<>());
            permsList = new ArrayList<>(menuList.size());
            for(SysMenuEntity menu : menuList){
                permsList.add(menu.getPerms());
            }
        }else{
            permsList = sysUserDao.queryAllPerms(user.getUserId());
        }
        //用户权限列表
        Set<String> permsSet = new HashSet<>();
        for(String perms : permsList){
            if(StringUtils.isBlank(perms)){
                continue;
            }
            permsSet.addAll(Arrays.asList(perms.trim().split(",")));
        }
        return permsSet;
    }

    @Override
    public SysUserTokenEntity queryByToken(String token) {
        return sysUserTokenDao.queryByToken(token);
    }

    @Override
    public SysUserEntity queryUser(Long userId) {
        SysUserEntity sysUserEntity = redisService.get(userInfoCKey + userId, SysUserEntity.class);
        if (null == sysUserEntity) {
            sysUserEntity = sysUserDao.queryObject(userId);
            redisService.set(userInfoCKey + userId, sysUserEntity, -1);
        }
        return sysUserEntity;
    }

}

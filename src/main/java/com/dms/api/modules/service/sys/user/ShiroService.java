package com.dms.api.modules.service.sys.user;

import com.dms.api.modules.entity.sys.user.SysUserEntity;
import com.dms.api.modules.entity.sys.user.SysUserTokenEntity;

import java.util.Set;

/**
 * shiro相关接口
 */
public interface ShiroService {

    /**
     * 获取用户权限列表
     */
    Set<String> getUserPermissions(SysUserEntity user);

    SysUserTokenEntity queryByToken(String token);

    /**
     * 根据用户ID，查询用户
     * @param userId
     */
    SysUserEntity queryUser(Long userId);
}

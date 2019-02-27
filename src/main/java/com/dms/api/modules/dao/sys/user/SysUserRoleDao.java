package com.dms.api.modules.dao.sys.user;

import com.dms.api.modules.base.BaseDao;
import com.dms.api.modules.entity.sys.user.SysUserRoleEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户与角色对应关系
 */
@Mapper
public interface SysUserRoleDao extends BaseDao<SysUserRoleEntity> {
	
	/**
	 * 根据用户ID，获取角色ID列表
	 */
	List<Long> queryRoleIdList(Long userId);

}

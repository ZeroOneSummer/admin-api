package com.dms.api.modules.dao.sys.role;

import com.dms.api.modules.base.BaseDao;
import com.dms.api.modules.entity.sys.role.SysRoleEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 角色管理
 * 
 */
@Mapper
public interface SysRoleDao extends BaseDao<SysRoleEntity> {
	
	/**
	 * 查询用户创建的角色ID列表
	 */
	List<Long> queryRoleIdList(Long createUserId);
}

package com.dms.api.modules.service.sys.user;

import java.util.List;

/**
 * 用户与角色对应关系
 * @author
 * @email @danpacmall.com
 * @date 2016年9月18日 上午9:43:24
 */
public interface SysUserRoleService {
	
	void saveOrUpdate(Long userId, List<Long> roleIdList);

	List<Long> queryRoleIdList(Long userId);
	
	void delete(Long userId);

}

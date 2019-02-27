package com.dms.api.modules.service.sys.user;

import java.util.List;
import java.util.Map;

import com.dms.api.modules.entity.sys.user.SysUserOrgEntity;

public interface SysUserOrgService {

	SysUserOrgEntity queryObject(Long id);
	
	List<SysUserOrgEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(SysUserOrgEntity bmOrganization);
	
	void update(SysUserOrgEntity bmOrganization);
	
	void delete(Long id);
	
	void deleteBatch(Long[] ids);

}

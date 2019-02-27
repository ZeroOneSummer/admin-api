package com.dms.api.modules.service.basis.org;


import com.dms.api.common.utils.Query;
import com.dms.api.modules.entity.basis.org.BmOrganizationEntity;
import com.dms.api.modules.entity.basis.org.BmSysOrganization;
import com.dms.api.modules.entity.basis.org.BmSysOrganizationWithParent;
import com.dms.api.modules.entity.sys.user.SysUserEntity;

import java.util.List;
import java.util.Map;

/**
 * 机构表
 * 
 */
public interface BmOrganizationService {
	
	BmOrganizationEntity queryObject(Long id);
	
	List<BmOrganizationEntity> queryList(Map<String, Object> map);
	
	List<BmSysOrganization> queryListByUser(SysUserEntity user);
	
	int queryTotal(Map<String, Object> map);
	
	void save(BmOrganizationEntity bmOrganization);
	
	void update(BmOrganizationEntity bmOrganization);
	
	void delete(Long id);
	
	void deleteBatch(Long[] ids);
	
	BmOrganizationEntity queryObjetByUserId(Long id);

	/**
	 * 获取订货分部指定级别下属机构
	 * @param queryParam
	 * @return
	 */
	List<BmSysOrganization> queryListInLevel(Query queryParam);

	/**
	 * 获取订货分部指定级别下属机构-有父级机构信息
	 * @param queryParam
	 * @return
	 */
	List<BmSysOrganizationWithParent> queryListInLevelWith(Query queryParam);

	/**
	 * 获取订货分部指定级别下属机构总数
	 * @param queryParam
	 * @return
	 */
	int queryListInLevelTotal(Query queryParam);

	/**
	 * 设置机构的人数
	 */
	void setUserCountWithOrg(List<BmSysOrganizationWithParent> bmSysOrganizations);

	List<Map<String,Object>> getUserCountWithOrg(List<String> ids);

	BmSysOrganization queryByOrgID(BmSysOrganization temp);
}

package com.dms.api.modules.service.sys.user.impl;

import java.util.List;
import java.util.Map;

import com.dms.api.modules.dao.sys.user.SysUserOrgDao;
import com.dms.api.modules.entity.sys.user.SysUserOrgEntity;
import com.dms.api.modules.service.sys.user.SysUserOrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysUserOrgServiceImpl implements SysUserOrgService {

	@Autowired
	private SysUserOrgDao sysUserOrgDao;

	@Override
	public SysUserOrgEntity queryObject(Long id) {
		return sysUserOrgDao.queryObject(id);
	}

	@Override
	public List<SysUserOrgEntity> queryList(Map<String, Object> map) {
		return sysUserOrgDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return sysUserOrgDao.queryTotal(map);
	}

	@Override
	public void save(SysUserOrgEntity bmOrganization) {
		sysUserOrgDao.save(bmOrganization);
	}

	@Override
	public void update(SysUserOrgEntity bmOrganization) {
		sysUserOrgDao.update(bmOrganization);
	}

	@Override
	public void delete(Long id) {
		sysUserOrgDao.delete(id);
	}

	@Override
	public void deleteBatch(Long[] ids) {
		sysUserOrgDao.deleteBatch(ids);
	}

}

package com.dms.api.modules.service.reserve.level.impl;


import com.dms.api.modules.dao.reserve.level.SysLevelapplyDao;
import com.dms.api.modules.entity.reserve.level.SysLevelapplyEntity;
import com.dms.api.modules.service.reserve.level.SysLevelapplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("sysLevelapplyService")
public class SysLevelapplyServiceImpl implements SysLevelapplyService {
	@Autowired
	private SysLevelapplyDao sysLevelapplyDao;
	
	@Override
	public SysLevelapplyEntity queryObject(Integer id){
		return sysLevelapplyDao.queryObject(id);
	}
	
	@Override
	public List<SysLevelapplyEntity> queryList(Map<String, Object> map){
		return sysLevelapplyDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return sysLevelapplyDao.queryTotal(map);
	}
	
	@Override
	public void save(SysLevelapplyEntity sysLevelapply){
		sysLevelapplyDao.save(sysLevelapply);
	}
	
	@Override
	public void update(SysLevelapplyEntity sysLevelapply){
		sysLevelapplyDao.update(sysLevelapply);
	}
	
	@Override
	public void delete(Integer id){
		sysLevelapplyDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		sysLevelapplyDao.deleteBatch(ids);
	}
	
}

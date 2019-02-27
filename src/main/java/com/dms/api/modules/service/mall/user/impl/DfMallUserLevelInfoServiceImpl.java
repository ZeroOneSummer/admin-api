package com.dms.api.modules.service.mall.user.impl;

import com.dms.api.modules.dao.mall.user.DfMallUserLevelInfoDao;
import com.dms.api.modules.entity.mall.user.DfMallUserLevelInfoEntity;
import com.dms.api.modules.service.mall.user.DfMallUserLevelInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("dfMallUserLevelInfoService")
public class DfMallUserLevelInfoServiceImpl implements DfMallUserLevelInfoService {

	@Autowired
	private DfMallUserLevelInfoDao dfMallUserLevelInfoDao;
	
	@Override
	public DfMallUserLevelInfoEntity queryObject(Long id){
		return dfMallUserLevelInfoDao.queryObject(id);
	}
	
	@Override
	public List<DfMallUserLevelInfoEntity> queryList(Map<String, Object> map){
		return dfMallUserLevelInfoDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return dfMallUserLevelInfoDao.queryTotal(map);
	}
	
	@Override
	public void save(DfMallUserLevelInfoEntity dfMallUserLevelInfo){
		dfMallUserLevelInfoDao.save(dfMallUserLevelInfo);
	}
	
	@Override
	public void update(DfMallUserLevelInfoEntity dfMallUserLevelInfo){
		dfMallUserLevelInfoDao.update(dfMallUserLevelInfo);
	}
	
	@Override
	public void delete(Long id){
		dfMallUserLevelInfoDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Long[] ids){
		dfMallUserLevelInfoDao.deleteBatch(ids);
	}
	
}

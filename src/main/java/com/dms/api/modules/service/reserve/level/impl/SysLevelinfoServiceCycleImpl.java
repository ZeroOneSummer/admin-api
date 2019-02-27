package com.dms.api.modules.service.reserve.level.impl;


import com.dms.api.modules.dao.reserve.level.SysLevelinfoCycleDao;
import com.dms.api.modules.entity.reserve.level.SysLevelinfoCycleEntity;
import com.dms.api.modules.service.reserve.level.SysLevelinfoCycleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("sysLevelinfoCycleService")
public class SysLevelinfoServiceCycleImpl implements SysLevelinfoCycleService {

	@Autowired
	private SysLevelinfoCycleDao sysLevelinfoCycleDao;
	
	@Override
	public SysLevelinfoCycleEntity queryObject(Integer id){
		return sysLevelinfoCycleDao.queryObject(id);
	}
	
	@Override
	public List<SysLevelinfoCycleEntity> queryList(Map<String, Object> map){
		return sysLevelinfoCycleDao.queryList(map);
	}

	@Override
	public void save(SysLevelinfoCycleEntity sysLevelinfo){
		sysLevelinfoCycleDao.save(sysLevelinfo);
	}
	
	@Override
	public void update(SysLevelinfoCycleEntity sysLevelinfo){
		sysLevelinfoCycleDao.update(sysLevelinfo);
	}
	
	@Override
	public void delete(Integer id){
		sysLevelinfoCycleDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		sysLevelinfoCycleDao.deleteBatch(ids);
	}

}

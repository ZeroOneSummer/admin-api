package com.dms.api.modules.service.reserve.level.impl;


import com.dms.api.common.utils.RedisService;
import com.dms.api.common.utils.RedisUtils;
import com.dms.api.modules.dao.reserve.level.SysLevelinfoDao;
import com.dms.api.modules.entity.reserve.level.SysLevelinfoEntity;
import com.dms.api.modules.service.reserve.level.SysLevelinfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("sysLevelinfoService")
public class SysLevelinfoServiceImpl implements SysLevelinfoService {
	private Logger logger= LoggerFactory.getLogger(getClass());

	@Autowired
	private SysLevelinfoDao sysLevelinfoDao;

	@Autowired
	private RedisService redisService;

	@Autowired
	private RedisUtils redisUtils;

	@Override
	public SysLevelinfoEntity queryObject(Integer id){
		return sysLevelinfoDao.queryObject(id);
	}

	@Override
	public List<SysLevelinfoEntity> queryList(Map<String, Object> map){
		return sysLevelinfoDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map){
		return sysLevelinfoDao.queryTotal(map);
	}

	@Override
	public void save(SysLevelinfoEntity sysLevelinfo){
		sysLevelinfoDao.save(sysLevelinfo);
		delRedisLevel();
	}

	@Override
	public void update(SysLevelinfoEntity sysLevelinfo){
		sysLevelinfoDao.update(sysLevelinfo);
		delRedisLevel();
	}

	@Override
	public void delete(Integer id){
		sysLevelinfoDao.delete(id);
		delRedisLevel();
	}

	@Override
	public void deleteBatch(Integer[] ids){
		sysLevelinfoDao.deleteBatch(ids);
		delRedisLevel();
	}

	@Override
	public void updateDfaultLevel(String levelcode) {
		sysLevelinfoDao.updateDfaultLevel(levelcode);
	}

	@Override
	public SysLevelinfoEntity getDefaultLevel() {
		return sysLevelinfoDao.getDefaultLevel();
	}

	@Override
	public String getCycleIdsByIds(Integer[] ids) {
		return sysLevelinfoDao.getCycleIdsByIds(ids);
	}

	public void delRedisLevel() {
		redisService.del("dfMallUserInfo");
	}

	@Override
	public void clearUserLevelCache(){
		logger.info("修改等级配置信息清除所有 用户等级缓存 key:ADMIN:USERLEVEL:*");
		redisUtils.batchDelete("ADMIN:USERLEVEL:*");
	}

}

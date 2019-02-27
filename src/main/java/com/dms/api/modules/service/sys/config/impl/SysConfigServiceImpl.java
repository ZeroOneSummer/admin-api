package com.dms.api.modules.service.sys.config.impl;

import com.alibaba.fastjson.JSONObject;
import com.dms.api.modules.entity.sys.config.SysConfigEntity;
import com.dms.api.modules.service.sys.config.SysConfigService;
import com.google.gson.Gson;
import com.dms.api.common.exception.DMException;
import com.dms.api.modules.dao.sys.config.SysConfigDao;
import com.dms.api.common.redis.SysConfigRedis;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service("sysConfigService")
public class SysConfigServiceImpl implements SysConfigService {
	Logger log = LoggerFactory.getLogger(SysConfigServiceImpl.class);

	@Autowired
	private SysConfigDao sysConfigDao;

	@Autowired
	private SysConfigRedis sysConfigRedis;
	
	@Override
	@Transactional
	public void save(SysConfigEntity config) {
		sysConfigDao.save(config);
		sysConfigRedis.saveOrUpdate(config);
	}

	@Override
	@Transactional
	public void update(SysConfigEntity config) {
		sysConfigDao.update(config);
		sysConfigRedis.saveOrUpdate(config);
	}

	@Override
	@Transactional
	public void updateValueByKey(String key, String value) {
		sysConfigDao.updateValueByKey(key, value);
		sysConfigRedis.delete(key);
	}

	@Override
	@Transactional
	public void deleteBatch(Long[] ids) {
		for(Long id : ids){
			SysConfigEntity config = queryObject(id);
			sysConfigRedis.delete(config.getKey());
		}
		sysConfigDao.deleteBatch(ids);
		
	}

	@Override
	public List<SysConfigEntity> queryList(Map<String, Object> map) {
		return sysConfigDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return sysConfigDao.queryTotal(map);
	}

	@Override
	public SysConfigEntity queryObject(Long id) {
		return sysConfigDao.queryObject(id);
	}

	@Override
	public String getValue(String key) {
		SysConfigEntity config = sysConfigRedis.get(key);
		log.info("sysconfig is {}", JSONObject.toJSONString(config));
		if(config == null){
			config = sysConfigDao.queryByKey(key);
			sysConfigRedis.saveOrUpdate(config);
		}

		return config == null ? null : config.getValue();
	}
	
	@Override
	public <T> T getConfigObject(String key, Class<T> clazz) {
		String value = getValue(key);
		if(StringUtils.isNotBlank(value)){
			return new Gson().fromJson(value, clazz);
		}

		try {
			return clazz.newInstance();
		} catch (Exception e) {
			throw new DMException("获取参数失败");
		}
	}

	@Override
	public List<SysConfigEntity> listPid() {
		return sysConfigDao.listPid();
	}

}

package com.dms.api.modules.dao.sys.config;

import com.dms.api.modules.base.BaseDao;
import com.dms.api.modules.entity.sys.config.SysConfigEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统配置信息
 * 
 */
@Mapper
public interface SysConfigDao extends BaseDao<SysConfigEntity> {

	SysConfigEntity queryByKey(String paramKey);

	int updateValueByKey(@Param("key") String key, @Param("value") String value);

	List<SysConfigEntity> listPid();
	
}

package com.dms.api.modules.dao.sys.log;

import com.dms.api.modules.base.BaseDao;
import com.dms.api.modules.entity.sys.log.SysLogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统日志
 * 
 */
@Mapper
public interface SysLogDao extends BaseDao<SysLogEntity> {
	
}

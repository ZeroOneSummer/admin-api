package com.dms.api.modules.service.sys.log;

import com.dms.api.modules.entity.sys.log.SysLogEntity;

import java.util.List;
import java.util.Map;

/**
 * 系统日志
 * @author
 * @email @danpacmall.com
 * @date 2017-03-08 10:40:56
 */
public interface SysLogService {
	
	SysLogEntity queryObject(Long id);
	
	List<SysLogEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(SysLogEntity sysLog);

	void delete(Long id);
	
	void deleteBatch(Long[] ids);

}

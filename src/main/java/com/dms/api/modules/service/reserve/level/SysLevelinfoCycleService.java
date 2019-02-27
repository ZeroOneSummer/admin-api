package com.dms.api.modules.service.reserve.level;


import com.dms.api.modules.entity.reserve.level.SysLevelinfoCycleEntity;

import java.util.List;
import java.util.Map;

/**
 * 等级周期限制信息表
 * 
 * @author
 * @email @danpacmall.com
 */
public interface SysLevelinfoCycleService {

	SysLevelinfoCycleEntity queryObject(Integer id);

	List<SysLevelinfoCycleEntity> queryList(Map<String, Object> map);

	void save(SysLevelinfoCycleEntity sysLevelinfo);
	
	void update(SysLevelinfoCycleEntity sysLevelinfo);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);

}

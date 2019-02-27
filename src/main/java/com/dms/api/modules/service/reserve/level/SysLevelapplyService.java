package com.dms.api.modules.service.reserve.level;


import com.dms.api.modules.entity.reserve.level.SysLevelapplyEntity;
import java.util.List;
import java.util.Map;

/**
 * 等级申请表
 * 
 * @author
 * @email @danpacmall.com
 * @date
 */
public interface SysLevelapplyService {
	
	SysLevelapplyEntity queryObject(Integer id);
	
	List<SysLevelapplyEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(SysLevelapplyEntity sysLevelapply);
	
	void update(SysLevelapplyEntity sysLevelapply);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);
}

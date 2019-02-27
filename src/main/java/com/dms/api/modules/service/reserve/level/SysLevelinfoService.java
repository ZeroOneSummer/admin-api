package com.dms.api.modules.service.reserve.level;


import com.dms.api.modules.entity.reserve.level.SysLevelinfoEntity;

import java.util.List;
import java.util.Map;

/**
 * 等级信息表
 *
 * @author
 * @email @danpacmall.com
 * @date 2017-08-28 13:46:02
 */
public interface SysLevelinfoService {

	SysLevelinfoEntity queryObject(Integer id);

	List<SysLevelinfoEntity> queryList(Map<String, Object> map);

	int queryTotal(Map<String, Object> map);

	void save(SysLevelinfoEntity sysLevelinfo);

	void update(SysLevelinfoEntity sysLevelinfo);

	void delete(Integer id);

	void deleteBatch(Integer[] ids);

	void updateDfaultLevel(String levelcode);

	SysLevelinfoEntity getDefaultLevel();

	String getCycleIdsByIds(Integer[] ids);

	public void clearUserLevelCache();

}

package com.dms.api.modules.service.mall.user;

import com.dms.api.modules.entity.mall.user.DfMallUserLevelInfoEntity;

import java.util.List;
import java.util.Map;

/**
 * 商城用户等级表
 * @author
 * @email @danpacmall.com
 * @date 2017-08-25 18:09:36
 */
public interface DfMallUserLevelInfoService {
	
	DfMallUserLevelInfoEntity queryObject(Long id);
	
	List<DfMallUserLevelInfoEntity> queryList(Map <String, Object> map);

	int queryTotal(Map <String, Object> map);
	
	void save(DfMallUserLevelInfoEntity dfMallUserLevelInfo);
	
	void update(DfMallUserLevelInfoEntity dfMallUserLevelInfo);
	
	void delete(Long id);
	
	void deleteBatch(Long[] ids);
}

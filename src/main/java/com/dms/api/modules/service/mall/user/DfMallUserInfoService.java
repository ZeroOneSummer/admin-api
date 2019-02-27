package com.dms.api.modules.service.mall.user;

import com.dms.api.common.utils.Query;
import com.dms.api.modules.entity.mall.user.DfMallUserInfoEntity;
import com.dms.api.modules.entity.mall.user.UserInfoEntity;
import com.dms.api.modules.entity.reserve.level.SysLevelapplyEntity;

import java.util.List;
import java.util.Map;

/**
 * 商城用户表
 * 
 * @author
 * @email @danpacmall.com
 * @date 2017-08-25 18:09:35
 */
public interface DfMallUserInfoService {
	
	DfMallUserInfoEntity queryObject(Long id);
	
	List<DfMallUserInfoEntity> queryList(Map <String, Object> map);

	int queryTotal(Map <String, Object> map);
	
	void save(DfMallUserInfoEntity dfMallUserInfo);
	
	void update(DfMallUserInfoEntity dfMallUserInfo);
	
	void delete(Long id);
	
	void deleteBatch(Long[] ids);

	List<Map> getUserAndLevel(Query query);

	void updateLevel(SysLevelapplyEntity sysLevelapply);

	List<UserInfoEntity> queryuUserInfoList(Query param);

	int queryuUserInfoListTotal(Query param);
}

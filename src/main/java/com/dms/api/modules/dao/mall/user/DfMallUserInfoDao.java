package com.dms.api.modules.dao.mall.user;

import com.dms.api.common.utils.Query;
import com.dms.api.modules.base.BaseDao;
import com.dms.api.modules.entity.mall.user.DfMallUserInfoEntity;
import com.dms.api.modules.entity.mall.user.UserInfoEntity;
import com.dms.api.modules.entity.reserve.level.SysLevelapplyEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 商城用户表
 * @author
 * @email @danpacmall.com
 * @date 2017-08-25 18:09:35
 */
@Mapper
public interface DfMallUserInfoDao extends BaseDao<DfMallUserInfoEntity> {

	List<Map> getUserAndLevel(Query query);

	DfMallUserInfoEntity getUserInfo(SysLevelapplyEntity sysLevelapply);

	void updateLevel(SysLevelapplyEntity sysLevelapply);

	Map<String, Object> queryUserInfoAndLevel(Map map);

	List<UserInfoEntity> queryuUserInfoList(Query query);

	int queryuUserInfoListTotal(Query query);

}

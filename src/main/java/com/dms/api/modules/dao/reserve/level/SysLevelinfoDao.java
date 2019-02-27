package com.dms.api.modules.dao.reserve.level;


import com.dms.api.modules.base.BaseDao;
import com.dms.api.modules.entity.reserve.level.SysLevelinfoEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 等级信息表
 *
 * @author
 * @email @danpacmall.com
 * @date 2017-08-28 13:46:02
 */
@Mapper
public interface SysLevelinfoDao extends BaseDao<SysLevelinfoEntity> {

	void updateDfaultLevel(String levelcode);

	SysLevelinfoEntity getDefaultLevel();

	String getCycleIdsByIds(Integer[] ids);

}

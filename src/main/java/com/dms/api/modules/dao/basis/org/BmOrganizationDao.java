package com.dms.api.modules.dao.basis.org;


import com.dms.api.modules.base.BaseDao;
import com.dms.api.modules.entity.basis.org.BmOrganizationEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * 机构表
 */
@Mapper
public interface BmOrganizationDao extends BaseDao<BmOrganizationEntity> {

    BmOrganizationEntity queryListByOrg(Map<String, Object> orgEntity);
}

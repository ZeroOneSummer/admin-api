package com.dms.api.modules.dao.basis.org;

import com.dms.api.modules.base.BaseDao;
import com.dms.api.modules.entity.basis.org.BmDealerInfo;
import com.dms.api.modules.entity.basis.org.TempEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface BmDealerInfoDao extends BaseDao<BmDealerInfo> {

    BmDealerInfo selectByPrimaryKey(Long id);

    List<BmDealerInfo> getDealers(@Param("dealerId") Long dealerId);

    List<BmDealerInfo> getDealerInfos(Map <String, Object> map);

    List<TempEntity> getByOrgIDInfos(Map <String, Object> map);
}
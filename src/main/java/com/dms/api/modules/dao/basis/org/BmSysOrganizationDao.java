package com.dms.api.modules.dao.basis.org;

import com.dms.api.common.utils.Query;
import com.dms.api.modules.entity.basis.org.BmSysOrganization;
import com.dms.api.modules.entity.basis.org.BmSysOrganizationWithParent;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BmSysOrganizationDao {

    BmSysOrganization selectByPrimaryKey(Long id);

    String selectDearlerIdByOrgId(String orgId);

    List<BmSysOrganization> queryListByOrg(BmSysOrganization orgEntity);

    List<BmSysOrganization> queryListInLevel(Query queryParam);

    List<BmSysOrganizationWithParent> queryListInLevelWith(Query queryParam);

    int queryListInLevelTotal(Query queryParam);

    List<Map<String,Object>> getUserCountWithOrg(List<String> ids);
}
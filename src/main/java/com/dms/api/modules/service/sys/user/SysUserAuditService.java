package com.dms.api.modules.service.sys.user;


import com.dms.api.modules.entity.sys.user.AuditUserGroup;
import com.dms.api.modules.entity.sys.user.SysUserAudit;

import java.util.List;
import java.util.Map;

public interface SysUserAuditService {

    List<SysUserAudit> queryList(Map<String, Object> map);

    List<AuditUserGroup> queryAuditUserGroups(String url);

    SysUserAudit queryObject(Object id);

    int add(String url, Map<String, String> reqParams, SysUserAudit sysUserAudit);

    int update(String url, Map<String, String> reqParams, SysUserAudit sysUserAudit);

    String queryPayId(String loginCode);
}

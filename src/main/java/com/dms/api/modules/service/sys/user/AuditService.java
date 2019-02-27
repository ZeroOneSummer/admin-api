package com.dms.api.modules.service.sys.user;

import com.alibaba.fastjson.JSONObject;
import com.dms.api.modules.entity.sys.user.AuditUserGroup;
import java.util.List;
import java.util.Map;

public interface AuditService {

    JSONObject getGroups(String url, Map<String, String> reqParams, JSONObject respJson);

    JSONObject addUsers(String url, Map<String, String> reqParams);

    JSONObject updateUserGroup(String url, Map<String, String> reqParams);

    List<AuditUserGroup> queryAuditUserGroups(String url, Map<String, String> reqParams);
}

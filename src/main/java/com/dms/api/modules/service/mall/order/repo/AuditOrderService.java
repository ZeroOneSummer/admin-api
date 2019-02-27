package com.dms.api.modules.service.mall.order.repo;

import com.alibaba.fastjson.JSONObject;
import com.dms.api.common.utils.R;

import java.util.List;
import java.util.Map;

public interface AuditOrderService {

    JSONObject queryList(String url, Map <String, String> reqParams);

    R check(String url, Map <String, String> reqParams, JSONObject syncParams, Map <String, String> smsParams, String loginCode);

    R sendSmsCode(String url, Map <String, String> reqParams, JSONObject syncParams);

    JSONObject getOrderDetail(String url, Map <String, String> reqParams);

    JSONObject syncAuditStatus(JSONObject reqParams);

    JSONObject repoAuditSms(Map <String, String> reqParams, List <Map <String, Object>> maps);
}

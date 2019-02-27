package com.dms.api.modules.service.reserve.userinfo;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * @author Administrator
 * @Description: TODO
 * @date 2018/11/14 11:35
 */
public interface UserParamChangeRecordService {

    JSONObject getUserParamChangeRecordList(String url, Map<String, Object> reqParams);
}

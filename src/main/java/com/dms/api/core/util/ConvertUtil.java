package com.dms.api.core.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConvertUtil {

    @Value("${iKey}")
    private String iKey;

    public String crypt(JSONObject params){
        params.put("key", iKey);
        String jsonStr = ParamSort.sort(params.toString());
        return MD5.getMD5Str(jsonStr).toUpperCase();
    }
}

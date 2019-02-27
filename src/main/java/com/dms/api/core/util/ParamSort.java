package com.dms.api.core.util;

import com.alibaba.fastjson.JSON;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class ParamSort {
    public static String sort(String signStr) {
        Map<String, String> map = json2Map(signStr);
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue() + "&");
        }
        return sb.toString().subSequence(0, sb.length() - 1) + "";
    }

    public static Map<String, String> json2Map(String jsonStr) {
        Map map = JSON.parseObject(jsonStr);
        Iterator<Map.Entry<String, String>> entries = map.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, String> entry = entries.next();
            Object value = entry.getValue();
            if (value == null || "".equals(value)) {
                entries.remove();
            }
        }
        return sortMapByKey(map);
    }

    public static Map<String, String> sortMapByKey(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, String> sortMap = new TreeMap<String, String>(new Comparator<String>() {
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        sortMap.putAll(map);
        return sortMap;
    }
}

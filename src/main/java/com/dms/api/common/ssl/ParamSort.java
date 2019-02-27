package com.dms.api.common.ssl;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import com.alibaba.fastjson.JSON;

/**
 * 参数排序(字典排序)
 * 
 * @author Ager
 *
 * @date 2017年6月30日 上午9:37:14
 */
public class ParamSort {

	public static String sort(String signStr) {
		// String signStr =
		// "{\"username\":\"ouzujie\",\"phone\":\"15113980520\",\"logincode\":\"15113980520\",\"serialcode\":\"\",\"idcard\":\"\",\"idcard_deadline\":\"\",\"password\":\"123456\",\"email\":\"769536244@qq.com\",\"sign\":\"\"}";
		Map<String, String> map = json2Map(signStr);
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			sb.append(entry.getKey() + "=" + entry.getValue() + "&");
		}
		return sb.toString().subSequence(0, sb.length() - 1) + "";
	}

	/**
	 * 将json字符串转换成 Map
	 * 
	 * @author Ager
	 * @date 2017年6月14日 下午8:34:26
	 */
	public static Map<String, String> json2Map(String jsonStr) {
		Map map = JSON.parseObject(jsonStr);
		// 去掉value为空的
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

	/**
	 * 使用 Map按key进行排序
	 * 
	 * @param map
	 * @return
	 */
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

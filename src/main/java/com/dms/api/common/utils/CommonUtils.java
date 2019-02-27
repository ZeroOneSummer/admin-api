package com.dms.api.common.utils;

import java.text.Collator;
import java.util.*;

public class CommonUtils {
    /**
     * 排序，默认升序
     * @param resultList
     * @param sort
     */
    public static  void listSort(List<HashMap<String, Object>> resultList,String sort) {
        Collections.sort(resultList, new Comparator<HashMap<String, Object>>() {
            @Override
            public int compare(HashMap<String, Object> o1, HashMap<String, Object> o2) {
                String name1=(String)o1.get( "dealerName");
                String name2=(String)o2.get( "dealerName");
                Collator instance = Collator.getInstance(Locale.CHINA);
                if (StringUtils.isBlank(sort)){
                 return instance.compare(name1, name2);
                }else if ("DESC".equals(sort.toUpperCase())) {
                    return instance.compare(name2, name1);
                } else {
                    return instance.compare(name1, name2);
                }

            }
        });
    }

    /**
     * 排序，默认升序
     * @param resultList
     * @param sort
     */
    public static  void listSortbyKey(List<HashMap<String, Object>> resultList,String key ,String sort) {
        Collections.sort(resultList, new Comparator<HashMap<String, Object>>() {
            @Override
            public int compare(HashMap<String, Object> o1, HashMap<String, Object> o2) {
                String name1=(String)o1.get( key);
                String name2=(String)o2.get( key);
                Collator instance = Collator.getInstance(Locale.CHINA);
                if (StringUtils.isBlank(sort)){
                    return instance.compare(name1, name2);
                }else if ("DESC".equals(sort.toUpperCase())) {
                    return instance.compare(name2, name1);
                } else {
                    return instance.compare(name1, name2);
                }

            }
        });
    }
}

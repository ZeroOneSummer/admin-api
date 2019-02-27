package com.dms.api.common.xss;

import com.dms.api.common.exception.DMException;
import org.apache.commons.lang.StringUtils;

/**
 * SQL过滤
 */
public class SQLFilter {

    private final static String[] keywords = {"master", "truncate", "insert", "select", "delete", "update", "declare", "alert", "drop"};

    /**
     * SQL注入过滤
     *
     * @param str 待验证的字符串
     */
    public static String sqlInject(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        //去掉'|"|;|\字符
        str = StringUtils.replace(str, "'", "");
        str = StringUtils.replace(str, "\"", "");
        str = StringUtils.replace(str, ";", "");
        str = StringUtils.replace(str, "\\", "");

        //转换成小写
        String str1 = str.toLowerCase();

        //判断是否包含非法字符
        for (String keyword : keywords) {
            if (str1.indexOf(keyword) != -1) {
                throw new DMException("包含非法字符");
            }
        }

        return str;
    }
}

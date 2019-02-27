package com.dms.api.common.utils;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils extends org.apache.commons.lang.StringUtils {
    public static String upperCaseByFirstChar(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String UUIDStrNoSplit() {
        String strTemp = UUID();
        StringBuffer sTemp = new StringBuffer();
        String[] temp = strTemp.split("-");
        for (int i = 0; i < temp.length; i++) {
            sTemp.append(temp[i]);
        }

        strTemp = sTemp.length() <= 0 ? strTemp : sTemp.toString();

        return strTemp;
    }

    public static String UUID() {
        String strTemp = UUID.randomUUID().toString();
        return strTemp;
    }


    /**
     * 下划线转驼峰法
     *
     * @param line       源字符串
     * @param smallCamel 大小驼峰,是否为小驼峰
     * @return 转换后的字符串
     */
    public static String underlineToCamel(String line, boolean smallCamel) {
        if (line == null || "".equals(line)) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        Pattern pattern = Pattern.compile("([A-Za-z\\d]+)(_)?");
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            String word = matcher.group();
            sb.append(smallCamel && matcher.start() == 0 ? Character.toLowerCase(word.charAt(0)) : Character.toUpperCase(word.charAt(0)));
            int index = word.lastIndexOf('_');
            if (index > 0) {
                sb.append(word.substring(1, index).toLowerCase());
            } else {
                sb.append(word.substring(1).toLowerCase());
            }
        }
        return sb.toString();
    }

    /**
     * 驼峰法转下划线
     *
     * @param line 源字符串
     * @return 转换后的字符串
     */
    public static String camelToUnderline(String line) {
        if (line == null || "".equals(line)) {
            return "";
        }
        line = String.valueOf(line.charAt(0)).toUpperCase().concat(line.substring(1));
        StringBuffer sb = new StringBuffer();
        Pattern pattern = Pattern.compile("[A-Z]([a-z\\d]+)?");
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            String word = matcher.group();
            sb.append(word.toLowerCase());
            sb.append(matcher.end() == line.length() ? "" : "_");
        }
        return sb.toString();
    }

    /**
     * 手机号屏蔽
     * @param phone
     * @return
     */
   public static String phoneShield(String phone){
       String result = phone.substring(0,3) + "***" + phone.substring(7);

       return result;
   }
}

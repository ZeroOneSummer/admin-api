package com.dms.api.common.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by on 2017/4/18.
 */
public class TimeUtil {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 一天的时间间隔（毫秒）
     */
    private final static Long dayTime = 24 * 60 * 60 * 1000L;

    public static void main(String args[]) {
        //当日
        System.out.printf("当日：");
        System.out.println(getCurrentday() + " 00:00:01" + "," + getCurrentday() + " 23:59:59");

        //昨天
        System.out.printf("昨天：");
        System.out.println(getYesteroday() + " 00:00:01" + "," + getYesteroday() + " 23:59:59");

        //本周
        System.out.printf("本周：");
        System.out.println(getCurrentMonday() + " 00:00:01" + "," + getPreviousSunday() + " 23:59:59");

        //上周
        System.out.printf("上周：");
        System.out.println(getCurrentMonday(-7) + " 00:00:01" + "," + getPreviousSunday(-7) + " 23:59:59");

        //本月
        System.out.printf("本月：");
        System.out.println(getMinMonthDate(getCurrentday()) + " 00:00:01" + "," + getMaxMonthDate(getCurrentday()) + " 23:59:59");

        //上月
        System.out.printf("上月：");
        System.out.println(getMinMonthDate(getCurrentday(), -1) + " 00:00:01" + "," + getMaxMonthDate(getCurrentday(), -1) + " 23:59:59");


    }

    // 获得本周一与当前日期相差的天数
    public static int getMondayPlus() {
        Calendar cd = Calendar.getInstance();
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 1) {
            return -6;
        } else {
            return 2 - dayOfWeek;
        }
    }

    //获取当天日期
    public static String getCurrentday() {
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, 0);
        Date monday = currentDate.getTime();
        String preMonday = dateFormat.format(monday);
        return preMonday;
    }


    public static int getMondayPlus(int days) {
        Calendar cd = Calendar.getInstance();
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 1) {
            return -6 + days;
        } else {
            return 2 - dayOfWeek + days;
        }
    }

    // 获得当前周- 周一的日期  
    public static String getCurrentMonday() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus);
        Date monday = currentDate.getTime();
        String preMonday = dateFormat.format(monday);
        return preMonday;
    }

    // 获得当前周- 周一的日期  
    public static String getCurrentMonday(int days) {
        int mondayPlus = getMondayPlus(days);
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus);
        Date monday = currentDate.getTime();
        String preMonday = dateFormat.format(monday);
        return preMonday;
    }


    // 获得当前周- 周日  的日期  
    public static String getPreviousSunday() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 6);
        Date monday = currentDate.getTime();
        String preMonday = dateFormat.format(monday);
        return preMonday;
    }

    // 获得当前周- 周日  的日期  
    public static String getPreviousSunday(int days) {
        int mondayPlus = getMondayPlus(days);
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 6);
        Date monday = currentDate.getTime();
        String preMonday = dateFormat.format(monday);
        return preMonday;
    }

    // 获得当前月--开始日期  
    public static String getMinMonthDate(String date) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(dateFormat.parse(date));
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
            return dateFormat.format(calendar.getTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 获得当前月--开始日期  
    public static String getMinMonthDate(String date, int month) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(dateFormat.parse(date));
            calendar.add(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
            return dateFormat.format(calendar.getTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 获得当前月--结束日期  
    public static String getMaxMonthDate(String date) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(dateFormat.parse(date));
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            return dateFormat.format(calendar.getTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getMaxMonthDate(String date, int month) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(dateFormat.parse(date));
            calendar.add(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            return dateFormat.format(calendar.getTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getYesteroday() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        return dateFormat.format(calendar.getTime());
    }


    public static List<String> getDateList(Map<String, Object> map) {
        List<String> dateList = new ArrayList<>();
        int limit = map.get("limit") == null ? -1 : Integer.parseInt(map.get("limit") + "");
        int page = map.get("page") == null ? -1 : Integer.parseInt(map.get("page") + "");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String nowSt = dtf.format(LocalDateTime.now());
        String startDateSt = (String) map.get("startTime");
        String endDateSt = (String) map.get("endTime");
        //流水记录最小时间
        String minDate = map.get("minDate") + "";
        //如果查询时间条件为空或者开始时间小于记录最小时间，或者结束时间大于当天则取默认时间
        startDateSt = startDateSt == null || "".equals(startDateSt) || startDateSt.compareTo(minDate) < 0 ? minDate : startDateSt.substring(0, 10);
        endDateSt = endDateSt == null || "".equals(endDateSt) || endDateSt.compareTo(nowSt) > 0 ? nowSt : endDateSt.substring(0, 10);
        Long startTime = 0L;
        Long endTime = 0L;
        try {
            startTime = df.parse(startDateSt).getTime();
            endTime = df.parse(endDateSt).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        LocalDateTime end = LocalDateTime.ofEpochSecond(endTime / 1000, 0, ZoneOffset.ofHours(8));
        Long interval = endTime - startTime;
        //计算查询出的日期
        if (interval >= 0) {
            int dayNum = Integer.parseInt((interval / dayTime) + "") + 1;
            //查询分页
            if (limit != -1 && page != -1) {
                dayNum = dayNum > limit * page ? limit : dayNum;
                for (int i = 0; i < dayNum; i++) {
                    Timestamp endTimestamp = Timestamp.valueOf(end);
                    LocalDateTime tmp = LocalDateTime.ofEpochSecond((endTimestamp.getTime() - ((i + limit * (page - 1)) * dayTime)) / 1000, 0, ZoneOffset.ofHours(8));
                    String tmpSt = dtf.format(tmp);
                    if (tmpSt.compareTo(startDateSt) < 0) {
                        break;
                    }
                    dateList.add(tmpSt);
                }
                //查询总数
            } else {
                for (int i = 0; i < dayNum; i++) {
                    Timestamp endTimestamp = Timestamp.valueOf(end);
                    LocalDateTime tmp = LocalDateTime.ofEpochSecond((endTimestamp.getTime() - (i * dayTime)) / 1000, 0, ZoneOffset.ofHours(8));
                    String tmpSt = dtf.format(tmp);
                    if (tmpSt.compareTo(startDateSt) < 0) {
                        break;
                    }
                    dateList.add(tmpSt);
                }
            }
        }
        return dateList;
    }
}  
package com.dms.api.common.utils;


import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期处理
 */
public class DateUtils {
    /**
     * 时间格式(yyyy-MM-dd)
     */
    public final static String DATE_PATTERN = "yyyy-MM-dd";
    /**
     * 时间格式(yyyy-MM-dd HH:mm:ss)
     */
    public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    //一天的毫秒数
    private final static long dayTime = 24 * 60 * 60 * 1000L;

    public static String format(Date date) {
        return format(date, DATE_PATTERN);
    }

    public static String format(Date date, String pattern) {
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(date);
        }
        return null;
    }

    public static String getToday() {
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        String datast = df.format(date);
        return datast;
    }

    public static String getTodayByFormat(String format) {
        DateFormat df = new SimpleDateFormat(format);
        Date date = new Date();
        String datast = df.format(date);
        return datast;
    }

    public static Date getTodayDate() {
        return getTodayDate(0, 0, 0);
    }

    public static Date getTodayDate(int hour, int minute, int second) {
        LocalDateTime localDateTime = LocalDateTime.now().withNano(0).withSecond(second).withMinute(minute).withHour(hour);
        Timestamp timestamp = Timestamp.valueOf(localDateTime);
        return new Date(timestamp.getTime());
    }

    public static Date getYesterday() {
        LocalDateTime localDateTime = LocalDateTime.now().withNano(0).withSecond(0).withMinute(0).withHour(0);
        Timestamp timestamp = Timestamp.valueOf(localDateTime);
        return new Date(timestamp.getTime() - dayTime);
    }

    public static String getYesterdayByFormat(String format) {
        LocalDateTime localDateTime = LocalDateTime.now().withNano(0).withSecond(0).withMinute(0).withHour(0);
        Timestamp timestamp = Timestamp.valueOf(localDateTime);
        Date date = new Date(timestamp.getTime() - dayTime);
        DateFormat df = new SimpleDateFormat(format);

        return df.format(date);
    }
    public static Date getTomorrow() {
        Date date = getTomorrow(0, 0, 0);
        return new Date(date.getTime() + dayTime);
    }

    public static Date getTomorrow(int hour, int minute, int second) {
        LocalDateTime localDateTime = LocalDateTime.now().withHour(hour).withMinute(minute).withSecond(second).withNano(0);
        Timestamp timestamp = Timestamp.valueOf(localDateTime);
        return new Date(timestamp.getTime() + dayTime);
    }

    public static String getTimeSt(String timest, int index) {
        int hour = Integer.parseInt(timest.split(" ")[1]);
        int tsencond = index * 5;
        int minute = tsencond / 60;
        int sencond = tsencond % 60;
        String sencondSt = sencond + "";
        String minuteSt = minute + "";
        String hourSt = hour + "";
        if (minute < 10) {
            minuteSt = "0" + minuteSt;
        }
        if (sencond < 10) {
            sencondSt = "0" + sencondSt;
        }
        if (minute == 60) {
            minuteSt = "00";
            hour++;
        }
        if (hour < 10) {
            hourSt = "0" + hourSt;
        }
//        System.out.println(timest.split(" ")[0]+" "+hour+":"+minuteSt+":"+sencondSt);
        return timest.split(" ")[0] + " " + hourSt + ":" + minuteSt + ":" + sencondSt;
    }

    /**
     * @author 40
     * date 2017/10/26
     * time  13:29
     * decription: 获取距离当前n分钟的时间戳
     */
    public static Long getNHourBefore(int minute) {
        int timeStampStep = minute * 60 * 1000;
        LocalDateTime l = LocalDateTime.now().withNano(0);
        Timestamp timestamp = Timestamp.valueOf(l);

        return timestamp.getTime() - timeStampStep;
    }

    /**
     * 获取当前时间之前多少分钟的时间戳
     * @param timestamp 当前时间戳
     * @param minute 需要减的分钟
     * @return
     */
    public static Long getNHourBefore(long timestamp, int minute) {
        int timeStampStep = minute*60*1000;

        return timestamp-timeStampStep;
    }

    /**
     * 添加时间,以分
     *
     * @param date 时间
     * @param value 需要加的分钟
     * @return Date
     * @author Ager
     */
    public static Date addTime(final Date date, final int value) {
        Calendar nowTime = Calendar.getInstance();
        // 分钟后的时间
        nowTime.add(Calendar.MINUTE, value);
        return nowTime.getTime();
    }

    /**
     * 获取时间戳对应的时间区间
     *
     * @param timestamp
     * @return
     */
    public static String getTimeIndex(Long timestamp) {
        Date date = new Date(timestamp);
        int second = date.getSeconds();
        Integer re = second % 10;
        second = re >= 5 ? second + 5 - re : second - re;
        date.setSeconds(second);
        return (date.getTime() + "").substring(0, 10) + "000";
    }

    /**
     * 获取时间戳
     *
     * @param localDateTime
     * @return
     */
    public static Long getTimestamp(LocalDateTime localDateTime) {
        Timestamp timestamp = Timestamp.valueOf(localDateTime);
        return timestamp.getTime();
    }

    public static Long getTTimeStamp(String tTime) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");
        LocalDateTime l = LocalDateTime.parse(tTime, df);
        return Timestamp.valueOf(l).getTime();
    }

    public static Long getTimeStamp(String tTime, String dfSt) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(dfSt);
        LocalDateTime l;
        l = LocalDateTime.parse(tTime, df);
        return Timestamp.valueOf(l).getTime();
    }

    public static String getTTimeIndex(String tTime) {
        return getTimeIndex(getTTimeStamp(tTime));
    }

    /**
     * @author 40
     * date 2017/11/14
     * time  14:20
     * decription: 查询时间小于六点，则查询昨天六点开始的数据
     */

    public static LocalDateTime getCustomQueryTime(Long timestamp) {
        LocalDateTime l = LocalDateTime.ofEpochSecond(timestamp / 1000, 0, ZoneOffset.ofHours(8));
        int lDay = l.getDayOfYear();
        int lHour = l.getHour();
        if (lHour < 6) {
            l = l.withDayOfYear(lDay - 1).withHour(6).withMinute(0).withSecond(0);
        } else {
            l = l.withHour(6).withMinute(0).withSecond(0);
        }
        return l;
    }

    /**
     * date2比date1多的天数
     * @param date1
     * @param date2
     * @return
     */
    public static int calcDayOffset(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1 = cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if (year1 != year2) {  //同一年
            int timeDistance = 0;
            for (int i = year1; i < year2; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {  //闰年
                    timeDistance += 366;
                } else {  //不是闰年

                    timeDistance += 365;
                }
            }
            return timeDistance + (day2 - day1);
        } else { //不同年
            return day2 - day1;
        }
    }

    /**
     * date2比date1多的周数
     * @param startTime
     * @param endTime
     * @return
     */
    public static int calcWeekOffset(Date startTime, Date endTime) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(startTime);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        dayOfWeek = dayOfWeek - 1;
        if (dayOfWeek == 0) dayOfWeek = 7;

        int dayOffset = calcDayOffset(startTime, endTime);

        int weekOffset = dayOffset / 7;
        int a;
        if (dayOffset > 0) {
            a = (dayOffset % 7 + dayOfWeek > 7) ? 1 : 0;
        } else {
            a = (dayOfWeek + dayOffset % 7 < 1) ? -1 : 0;
        }
        weekOffset = weekOffset + a;
        return weekOffset;
    }

    public static String getTraDay(){
        Calendar calendar = Calendar.getInstance();
        int i = calendar.get(Calendar.HOUR_OF_DAY);
        if (i >= 5 && i <= 7 ){
            calendar.add(Calendar.DAY_OF_MONTH,1);
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.format(calendar.getTime());
        return df.format(calendar.getTime());
    }

}

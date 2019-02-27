package com.dms.api.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CreateWhereSQL {

    public static final String TIME_TYPE_CURRENTDAY = "CURRENTDAY";
    public static final String TIME_TYPE_YESTERODAY = "YESTERODAY";
    public static final String TIME_TYPE_CURRENTWEEK = "CURRENTWEEK";
    public static final String TIME_TYPE_PREWEEK = "PREWEEK";
    public static final String TIME_TYPE_CURRENTMONTH = "CURRENTMONTH";
    public static final String TIME_TYPE_PREMONTH = "PREMONTH";

    //订单类型
    public static final String ORDER_TYPE_B = "b";
    public static final String ORDER_TYPE_S = "s";
    public static final String ORDER_TYPE_ORDER_TYPE_CLOSE = "ORDER_TYPE_CLOSE";
    public static final String ORDER_TYPE_ORDER_TYPE_OPEN = "ORDER_TYPE_OPEN";
    public static final String ORDER_TYPE_ORDER_TYPE_DELIVERY = "ORDER_TYPE_DELIVERY";
    public static final String ORDER_TYPE_ORDER_TYPE_SELL = "ORDER_TYPE_SELL";


    public static final String TYPE_START = "start"; //开始时间
    public static final String TYPE_END = "end";    //XIUSHI

    public static final String DATE_TRANDE = "TRANDE";//预订日
    public static final String DATE_NATRU = "NATRU";//自然日
    public static final String DATE_WEEK = "WEEK";//工作日


    private static final SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取预订日日期时间
     * @param param
     * @return
     */
    public static Map<String,Object> get(Map<String,Object> param){
        Map<String,Object > date = new HashMap<>();

        Object timeType = param.get("timeType");
        if(timeType == null){
            return null;
        } else {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int min = calendar.get(Calendar.MINUTE);
            int second = calendar.get(Calendar.SECOND);

            String datestr = "";
            String endstr = "";
            switch ((String)timeType){
                case TIME_TYPE_CURRENTDAY:
                    calendar.set(Calendar.HOUR_OF_DAY,8);
                    calendar.set(Calendar.MINUTE,0);
                    calendar.set(Calendar.SECOND,0);
                    datestr = dataFormat.format(calendar.getTime());
                    date.put("startTime",datestr);

                    calendar.add(Calendar.DAY_OF_MONTH,1);
                    calendar.set(Calendar.HOUR_OF_DAY,4);
                    calendar.set(Calendar.MINUTE,0);
                    calendar.set(Calendar.SECOND,0);
                     endstr = dataFormat.format(calendar.getTime());
                    date.put("endTime",endstr);
                    break;
                case TIME_TYPE_YESTERODAY:
                    calendar.add(Calendar.DAY_OF_MONTH,-1);

                    calendar.set(Calendar.HOUR_OF_DAY,8);
                    calendar.set(Calendar.MINUTE,0);
                    calendar.set(Calendar.SECOND,0);
                    datestr = dataFormat.format(calendar.getTime());
                    date.put("startTime",datestr);

                    calendar.add(Calendar.DAY_OF_MONTH,1);
                    calendar.set(Calendar.HOUR_OF_DAY,4);
                    calendar.set(Calendar.MINUTE,0);
                    calendar.set(Calendar.SECOND,0);
                    endstr = dataFormat.format(calendar.getTime());
                    date.put("endTime",endstr);
                    break;
                case TIME_TYPE_CURRENTWEEK:
                    Calendar weekMonday = getWeekMonday(TYPE_START, dataFormat, calendar);
                    date.put("startTime",dataFormat.format(weekMonday.getTime()));

                    Calendar weekFriday = getWeekFriday(TYPE_END, dataFormat, calendar);
                    weekFriday.add(Calendar.DAY_OF_WEEK,1);
                    date.put("endTime",dataFormat.format(weekFriday.getTime()));
                    break;
                case TIME_TYPE_PREWEEK:
                    Calendar lastTimeTursdayl = getLastTimeTursdayl(TYPE_START, dataFormat, calendar);
                    date.put("startTime",dataFormat.format(lastTimeTursdayl.getTime()));

                    Calendar lastTimeFriday = getLastTimeFriday(TYPE_END, dataFormat, calendar);
                    lastTimeFriday.add(Calendar.DAY_OF_WEEK,1);
                    date.put("endTime",dataFormat.format(lastTimeFriday.getTime()));
                    break;
                case TIME_TYPE_CURRENTMONTH:
                    Calendar monthFirstDay = getMonthFirstDay(TYPE_START,dataFormat,calendar);
                    date.put("startTime",dataFormat.format(monthFirstDay.getTime()));

                    Calendar lastMonthLastDay = getLastMonthLastDay(TYPE_END,dataFormat,calendar);
                    lastMonthLastDay.add(Calendar.DAY_OF_MONTH,1);
                    date.put("endTime",dataFormat.format(lastMonthLastDay.getTime()));
                    break;
                case TIME_TYPE_PREMONTH:
                    calendar.add(Calendar.MONTH,-1);

                    Calendar premonthFirstDay = getMonthFirstDay(TYPE_START,dataFormat,calendar);
                    date.put("startTime",dataFormat.format(premonthFirstDay.getTime()));

                    Calendar prelastMonthLastDay = getLastMonthLastDay(TYPE_END,dataFormat,calendar);
                    prelastMonthLastDay.add(Calendar.DAY_OF_MONTH,1);
                    date.put("endTime",dataFormat.format(prelastMonthLastDay.getTime()));
                    break;

                default: break;
            }
        }

        return date;
    }

    public static Map<String,Object > getNaturly(Map<String,Object> param){
        Map<String,Object > date = new HashMap<>();

        Object timeType = param.get("timeType");
        if(timeType == null){
            return null;
        } else {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int min = calendar.get(Calendar.MINUTE);
            int second = calendar.get(Calendar.SECOND);

            String datestr = "";
            String endstr = "";
            switch ((String)timeType){
                case TIME_TYPE_CURRENTDAY:
                    calendar.set(Calendar.HOUR_OF_DAY,0);
                    calendar.set(Calendar.MINUTE,0);
                    calendar.set(Calendar.SECOND,0);
                    datestr = dataFormat.format(calendar.getTime());
                    date.put("startTime",datestr);

                    calendar.set(Calendar.HOUR_OF_DAY,23);
                    calendar.set(Calendar.MINUTE,59);
                    calendar.set(Calendar.SECOND,59);
                    endstr = dataFormat.format(calendar.getTime());
                    date.put("endTime",endstr);
                    break;
                case TIME_TYPE_YESTERODAY:
                    calendar.add(Calendar.DAY_OF_MONTH,-1);

                    calendar.set(Calendar.HOUR_OF_DAY,0);
                    calendar.set(Calendar.MINUTE,0);
                    calendar.set(Calendar.SECOND,0);
                    datestr = dataFormat.format(calendar.getTime());
                    date.put("startTime",datestr);

                    calendar.set(Calendar.HOUR_OF_DAY,23);
                    calendar.set(Calendar.MINUTE,59);
                    calendar.set(Calendar.SECOND,59);
                    endstr = dataFormat.format(calendar.getTime());
                    date.put("endTime",endstr);
                    break;
                case TIME_TYPE_CURRENTWEEK:
                    Calendar weekMonday = getWeekMonday(TYPE_START, dataFormat, calendar);
                    weekMonday.set(Calendar.HOUR_OF_DAY,0);
                    weekMonday.set(Calendar.MINUTE,0);
                    weekMonday.set(Calendar.SECOND,0);
                    date.put("startTime",dataFormat.format(weekMonday.getTime()));

                    Calendar weekFriday = getLastWeekDay(TYPE_END, dataFormat, calendar);
                    weekFriday.add(Calendar.DAY_OF_WEEK,1);
                    weekFriday.set(Calendar.HOUR_OF_DAY,23);
                    weekFriday.set(Calendar.MINUTE,59);
                    weekFriday.set(Calendar.SECOND,59);
                    date.put("endTime",dataFormat.format(weekFriday.getTime()));
                    break;
                case TIME_TYPE_PREWEEK:
                    Calendar lastTimeTursdayl = getLastTimeTursdayl(TYPE_START, dataFormat, calendar);
                    lastTimeTursdayl.set(Calendar.HOUR_OF_DAY,0);
                    lastTimeTursdayl.set(Calendar.MINUTE,0);
                    lastTimeTursdayl.set(Calendar.SECOND,0);
                    date.put("startTime",dataFormat.format(lastTimeTursdayl.getTime()));

                    Calendar lastTimeFriday = getLastWeekDay(TYPE_END, dataFormat, calendar);
                    lastTimeFriday.add(Calendar.DAY_OF_WEEK,-6);
                    lastTimeFriday.set(Calendar.HOUR_OF_DAY,23);
                    lastTimeFriday.set(Calendar.MINUTE,59);
                    lastTimeFriday.set(Calendar.SECOND,59);
                    date.put("endTime",dataFormat.format(lastTimeFriday.getTime()));
                    break;
                case TIME_TYPE_CURRENTMONTH:
                    Calendar monthFirstDay = getMonthFirstDay(TYPE_START,dataFormat,calendar);
                    monthFirstDay.set(Calendar.HOUR_OF_DAY,0);
                    monthFirstDay.set(Calendar.MINUTE,0);
                    monthFirstDay.set(Calendar.SECOND,0);
                    date.put("startTime",dataFormat.format(monthFirstDay.getTime()));

                    Calendar lastMonthLastDay = getLastMonthLastDay(TYPE_END,dataFormat,calendar);
                    lastMonthLastDay.set(Calendar.HOUR_OF_DAY,23);
                    lastMonthLastDay.set(Calendar.MINUTE,59);
                    lastMonthLastDay.set(Calendar.SECOND,59);
                    date.put("endTime",dataFormat.format(lastMonthLastDay.getTime()));
                    break;
                case TIME_TYPE_PREMONTH:
                    calendar.add(Calendar.MONTH,-1);

                    Calendar premonthFirstDay = getMonthFirstDay(TYPE_START,dataFormat,calendar);
                    premonthFirstDay.set(Calendar.HOUR_OF_DAY,0);
                    premonthFirstDay.set(Calendar.MINUTE,0);
                    premonthFirstDay.set(Calendar.SECOND,0);
                    date.put("startTime",dataFormat.format(premonthFirstDay.getTime()));

                    Calendar prelastMonthLastDay = getLastMonthLastDay(TYPE_END,dataFormat,calendar);
                    prelastMonthLastDay.set(Calendar.HOUR_OF_DAY,23);
                    prelastMonthLastDay.set(Calendar.MINUTE,59);
                    prelastMonthLastDay.set(Calendar.SECOND,59);
                    date.put("endTime",dataFormat.format(prelastMonthLastDay.getTime()));
                    break;

                default: break;
            }
        }

        return date;
    }

    /**
     * 根据日期获取日期的完整时间（yyyy-MM-dd hh:mm:ss）
     * @param type 预订类型
     * @param dateStr  日期（yyyy-MM-dd）
     * @return
     */
    public static Calendar setDateTime (String type,String dateStr){
        Calendar cal = Calendar.getInstance();
        try {
            SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
            Date parse = s.parse(dateStr);
            cal.setTime(parse);

            if (TYPE_START.equals(type)){
                cal.set(Calendar.HOUR_OF_DAY,8);
            } else if (TYPE_END.equals(type)) {
                cal.add(Calendar.DAY_OF_MONTH,1);
                cal.set(Calendar.HOUR_OF_DAY,4);
            }

            cal.set(Calendar.MINUTE,0);
            cal.set(Calendar.SECOND,0);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return cal;
    }

    /**
     * 构造时间条件
     * @param query
     * @return
     */
    public static Map<String,Object > getTimeForWhereSQL(Map<String,Object > query){
        Map<String,Object > result = new HashMap<>();
        String timeType = (String) query.get("timeType");//时间条件
        String dateType = (String) query.get("dateType");//日期时间类型

        if (!StringUtils.isEmpty(timeType)){

            Map<String, Object> stringObjectMap =  null;

            if (!StringUtils.isEmpty(dateType) &&  DATE_TRANDE.equals(dateType)){
                stringObjectMap = CreateWhereSQL.get(query);//构造时间范围；
            }else if (!StringUtils.isEmpty(dateType) &&  DATE_NATRU.equals(dateType)){
                stringObjectMap =  CreateWhereSQL.getNaturly(query);
            }

            if (stringObjectMap != null && stringObjectMap.size() > 0){
                result.put("startTime",stringObjectMap.get("startTime"));
                result.put("endTime",stringObjectMap.get("endTime"));

            }
        } else {
            String starttime = (String) query.get("startTime");
            if (!StringUtils.isEmpty(starttime)){
                Calendar dateTemp =  CreateWhereSQL.setDateTime(CreateWhereSQL.TYPE_START,starttime);
                if(!StringUtils.isEmpty(dateType) &&  DATE_TRANDE.equals(dateType)){
                    result.put("startTime", dataFormat.format(dateTemp.getTime()));
                } else if (!StringUtils.isEmpty(dateType) &&  DATE_NATRU.equals(dateType)){ //自然日
                    dateTemp.set(Calendar.HOUR_OF_DAY,0);
                    dateTemp.set(Calendar.MINUTE,0);
                    dateTemp.set(Calendar.SECOND,0);
                    result.put("startTime", dataFormat.format(dateTemp.getTime()));
                }
            }

            String endtime = (String) query.get("endTime");
            if (!StringUtils.isEmpty(endtime)){
                Calendar dateTemp =  CreateWhereSQL.setDateTime(CreateWhereSQL.TYPE_END,endtime);
                if(!StringUtils.isEmpty(dateType) &&  DATE_TRANDE.equals(dateType)){
                    result.put("endTime", dataFormat.format(dateTemp.getTime()));
                } else if (!StringUtils.isEmpty(dateType) &&  DATE_NATRU.equals(dateType)){ //自然日
                    dateTemp.add(Calendar.DAY_OF_MONTH,-1);
                    dateTemp.set(Calendar.HOUR_OF_DAY,23);
                    dateTemp.set(Calendar.MINUTE,59);
                    dateTemp.set(Calendar.SECOND,59);
                    result.put("endTime", dataFormat.format(dateTemp.getTime()));
                }
            }
        }

        return result;
    }
// 订单状态/订单类型
    public static Map<String,Object > getOrderStatusAndTypeForWhereSQL(Map<String,Object > query){
        Map<String,Object > result = new HashMap<>();
        String orderType = (String) query.get("orderType");
        String orderStatus = (String) query.get("orderStatus");

        result.put("bsCode",orderType);
        result.put("orderType",orderStatus);
        return result;
    }

    /**
     * 构造in条件
     * @param keySplit
     * @return
     */
    public static List<Object> getInForWhereSQL(String... keySplit){
            List<Object > list = null;
        if (keySplit != null){
            list = new ArrayList<>();

            String result = "";
            for (String str:keySplit){
                list.add(str);
            }



        }

        return list != null && list.size() > 0 ?list:null;
    }


    /**
     * 本周一
     * @param type
     * @param formater
     * @param cal
     * @return
     */
    private static Calendar getWeekMonday(String type,SimpleDateFormat formater,Calendar cal){
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setTime(new Date());
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        if (TYPE_START.equals(type)){
            cal.set(Calendar.HOUR_OF_DAY,8);
        } else if (TYPE_END.equals(type)) {
            cal.set(Calendar.HOUR_OF_DAY,4);
        }

        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);

        return cal;
//        Date first=cal.getTime();
//        return formater.format(first);
    }
    /**
     * 本周周五日期
     * @return
     */
    private  static Calendar getWeekFriday(String type,SimpleDateFormat formater,Calendar cal){
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek() + 4);
//        cal.add(Calendar.DAY_OF_WEEK,1);
        if (TYPE_START.equals(type)){
            cal.set(Calendar.HOUR_OF_DAY,8);
        } else if (TYPE_END.equals(type)) {
            cal.set(Calendar.HOUR_OF_DAY,4);
        }

        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);

        return cal;
//        Date last=cal.getTime();
//        return  formater.format(last);
    }

    /**
     * 上周周五（结算为后一天）
     *
     * @return
     * @author zhaoxuepu
     */
    private  static Calendar getLastTimeFriday(String type,SimpleDateFormat formater,Calendar cal) {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        int dayOfWeek = calendar1.get(Calendar.DAY_OF_WEEK) - 1;
        int offset2 = 5 - dayOfWeek;
        calendar2.add(Calendar.DATE, offset2 - 7);
        if (TYPE_START.equals(type)){
            calendar2.set(Calendar.HOUR_OF_DAY,8);
        } else if (TYPE_END.equals(type)) {
            calendar2.set(Calendar.HOUR_OF_DAY,4);
        }

        calendar2.set(Calendar.MINUTE,0);
        calendar2.set(Calendar.SECOND,0);
//        calendar2.add(Calendar.DAY_OF_WEEK,1); //停止预订时间加一天
        return calendar2;
//        String lastEndDate = formater.format(calendar2.getTime());
//        return lastEndDate;
    }

    /**
     * 上周周一
     *
     * @return
     * @author zhaoxuepu
     */
    private static Calendar getLastTimeTursdayl(String type,SimpleDateFormat formater,Calendar cal) {
        Calendar calendar1 = Calendar.getInstance();
        int dayOfWeek = calendar1.get(Calendar.DAY_OF_WEEK) - 1;
        int offset1 = 1 - dayOfWeek;
        int offset2 = 5 - dayOfWeek;
        calendar1.add(Calendar.DATE, offset1 - 7);
        if (TYPE_START.equals(type)){
            calendar1.set(Calendar.HOUR_OF_DAY,8);
        } else if (TYPE_END.equals(type)) {
            calendar1.set(Calendar.HOUR_OF_DAY,4);
        }

        calendar1.set(Calendar.MINUTE,0);
        calendar1.set(Calendar.SECOND,0);

        return calendar1;
//        String lastBeginDate = formater.format(calendar1.getTime());
//        return lastBeginDate ;
    }

    /**
     * 指定月第一天
     * @param type
     * @param formater
     * @param cal
     * @return
     */
    private static Calendar getMonthFirstDay(String type,SimpleDateFormat formater,Calendar cal){
        cal.set(Calendar.DAY_OF_MONTH, 1);
        if (TYPE_START.equals(type)){
            cal.set(Calendar.HOUR_OF_DAY,8);
        } else if (TYPE_END.equals(type)) {
            cal.set(Calendar.HOUR_OF_DAY,4);
        }

        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        return cal;
//        String last = formater.format(cal.getTime());
//        return last;
    }

    /**
     * 指定月最后一天（结算为后一天）
     * @param type
     * @param formater
     * @param cal
     * @return
     */
    private static Calendar getLastMonthLastDay(String type,SimpleDateFormat formater,Calendar cal){
        int MaxDay=cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH,MaxDay);

//        cal.add(Calendar.DAY_OF_MONTH,1);

        if (TYPE_START.equals(type)){
            cal.set(Calendar.HOUR_OF_DAY,8);
        } else if (TYPE_END.equals(type)) {
            cal.set(Calendar.HOUR_OF_DAY,4);
        }

        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);


        return cal;
//        String gtime = formater.format(cal.getTime()); //上月最后一天
//        return gtime;

    }

    private static Calendar getLastWeekDay(String type,SimpleDateFormat formater,Calendar cal){
        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_WEEK);
        cal.set(Calendar.DAY_OF_WEEK,maxDay);

        if (TYPE_START.equals(type)){
            cal.set(Calendar.HOUR_OF_DAY,8);
        } else if (TYPE_END.equals(type)) {
            cal.set(Calendar.HOUR_OF_DAY,4);
        }

        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);


        return cal;
    }
}

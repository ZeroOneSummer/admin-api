package com.dms.api.common.utils;

import com.dms.api.common.xss.SQLFilter;
import com.dms.api.core.constants.ConstantTable;
import org.apache.commons.lang.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 查询参数
 *
 */
public class Query extends LinkedHashMap<String, Object> {
	private static final long serialVersionUID = 1L;
	//当前页码
    private int page;
    //每页条数
    private int limit;

    public Query(Map<String, Object> params){

        this.putAll(params);
        if(null != this.get("timeType") && !"".equals(this.get("timeType"))){
           setTime(this);
        }
        //分页参数
        this.page = Integer.parseInt(params.get("page").toString());
        this.limit = Integer.parseInt(params.get("limit").toString());
        this.put("offset", (page - 1) * limit);
        this.put("page", page);
        this.put("limit", limit);

        //防止SQL注入（因为sidx、order是通过拼接SQL实现排序的，会有SQL注入风险）
        String sidx = (String)params.get("sidx");
        String order = (String)params.get("order");
        if(StringUtils.isNotBlank(sidx)){
            this.put("sidx", SQLFilter.sqlInject(sidx));
        }
        if(StringUtils.isNotBlank(order)){
            this.put("order", SQLFilter.sqlInject(order));
        }

    }

    public Query() {
    	
    }
    

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     * 设置查询条件开始，结束时间
     *
     * @param query
     * @return
     */
    private void setTime(Query query) {
        String startTime = "";
        String endTime = "";
        {
            String timeType = (String) query.get("timeType");
            //判断传过来的时间区间，进行赋值
            if (ConstantTable.CURRENTDAY.equals(timeType)) {
                //当天
                startTime = TimeUtil.getCurrentday();
                endTime = startTime;
            } else if (ConstantTable.YESTERODAY.equals(timeType)) {
                //昨天
                startTime = TimeUtil.getYesteroday();
                endTime = startTime;
            } else if (ConstantTable.CURRENTWEEK.equals(timeType)) {
                startTime = TimeUtil.getCurrentMonday();
//                endTime = TimeUtil.getPreviousSunday();
                endTime = TimeUtil.getCurrentday();
            } else if (ConstantTable.PREWEEK.equals(timeType)) {
                startTime = TimeUtil.getCurrentMonday(-7);
                endTime = TimeUtil.getPreviousSunday(-7);
            } else if (ConstantTable.CURRENTMONTH.equals(timeType)) {
                startTime = TimeUtil.getMinMonthDate(TimeUtil.getCurrentday());
//                endTime = TimeUtil.getMaxMonthDate(TimeUtil.getCurrentday());
                endTime = TimeUtil.getCurrentday();
            } else if (ConstantTable.PREMONTH.equals(timeType)) {
                startTime = TimeUtil.getMinMonthDate(TimeUtil.getCurrentday(), -1);
                endTime = TimeUtil.getMaxMonthDate(TimeUtil.getCurrentday(), -1);
            }
        }
        query.put("startTime", startTime + " 00:00:00");
        query.put("endTime", endTime + " 23:59:59");
    }
}

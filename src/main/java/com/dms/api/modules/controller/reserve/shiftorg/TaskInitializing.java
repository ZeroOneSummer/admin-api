package com.dms.api.modules.controller.reserve.shiftorg;

import com.dms.api.common.utils.DateUtils;
import com.dms.api.common.utils.SpringContextUtils;
import com.dms.api.core.constants.ConstantTable;
import com.dms.api.modules.dao.sys.config.SysConfigDao;
import com.dms.api.modules.entity.sys.config.SysConfigEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 任务数据初始化
 * @author mok
 */
public class TaskInitializing {

    private final static Logger logger = LoggerFactory.getLogger(TaskInitializing.class);

    /**
     * 机构转移任务数据初始化：转移截止时间/次日执行时间 初始化
     */
    public static void initUserShiftOrg(){

        SysConfigDao sysConfigDao = SpringContextUtils.getBean(SysConfigDao.class);
        SysConfigEntity configStartTime = sysConfigDao.queryByKey(ConstantTable.CONFIG_CACHE_KEY_SHIFT_ORG_START_TIME);

        if (configStartTime!=null&& org.apache.commons.lang3.StringUtils.isNotBlank(configStartTime.getValue())){
            String starttime=configStartTime.getValue();
            String[] timefield = starttime.split(":");
            int hour = Integer.valueOf(timefield[0]);
            int minute = Integer.valueOf(timefield[1]);
            int second = Integer.valueOf(timefield[2]);
            UserShiftOrgTask.tomorrow = DateUtils.getTomorrow(hour, minute, second);//DateUtils.getTodayDate(hour, minute, second).getTime();
        }



        SysConfigEntity configEndTime = sysConfigDao.queryByKey(ConstantTable.CONFIG_CACHE_KEY_SHIFT_ORG_END_TIME);

        if (configEndTime!=null&& org.apache.commons.lang3.StringUtils.isNotBlank(configEndTime.getValue())){
            String endtime=configEndTime.getValue();
            String[] timefield = endtime.split(":");
            int hour = Integer.valueOf(timefield[0]);
            int minute = Integer.valueOf(timefield[1]);
            int second = Integer.valueOf(timefield[2]);
            UserShiftOrgTask.deadline = DateUtils.getTodayDate(hour, minute, second).getTime();
        }

        logger.info("任务时间初始化 开始时间:{},截止时间:{}",UserShiftOrgTask.tomorrow,UserShiftOrgTask.deadline);
    }
}

package com.dms.api.modules.controller.report.mallrun;

import cn.hutool.core.date.DateUtil;
import com.dms.api.common.utils.R;
import com.dms.api.common.utils.StringUtils;
import com.dms.api.modules.base.AbstractController;
import com.dms.api.modules.service.report.mallrun.MallRunTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/mallRunTask")
public class MallRunTaskController extends AbstractController {

    @Autowired
    private MallRunTaskService mallRunTaskService;

    /**
     * 生成结算表数据（综合:日、月）传参：creWay[CURR-当前，ALL-所有(sTime,eTime)]
     * @return
     */
    @RequestMapping("/createSettlementData")
    public R createSettlementData(@RequestParam Map<String, Object> map){

        logger.info("mallRunTask/createSettlementData接口入参：{}", map);

        //参数校验
        if(!this.checkPublicParams(map)){
            return R.error("必要参数缺失");
        }

        //时间校验
        if (StringUtils.equalsIgnoreCase("ALL", (String)map.get("creWay"))){
            switch (this.checkTime(map)){
                case 1: return R.error("结束日期必须小于当前日期");
                case 2: return R.error("起始日期必须小于结束日期，且月份至少间隔1个月");
                default: break;
            }
        }

        try {
            mallRunTaskService.createSettlementData(map);
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug("生成结算数据发生异常");
        }

        return R.ok("SUCCESS");
    }

    /**
     * 生成结算累计表数据（综合:日、月）传参：creWay[CURR-当前，ALL-所有]
     * @return
     */
    @RequestMapping("/createSettlementDataTotal")
    public R createSettlementDataTotal(@RequestParam Map<String, Object> map){

        logger.info("mallRunTask/createSettlementDataTotal接口入参：{}", map);

        //参数校验
        String creWay = map.get("creWay") == null ? null : (String)map.get("creWay");
        if (StringUtils.isBlank(creWay)){
            logger.debug("必要参数缺失");
            return R.error("必要参数缺失");
        }

        try {
            mallRunTaskService.createSettlementDataTotal(map);
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug("生成累计结算数据发生异常");
        }

        return R.ok("SUCCESS");
    }

    /**
     * 公共参数校验
     * @return
     */
    private Boolean checkPublicParams(Map<String, Object> map){

        //参数校验
        String creWay = map.get("creWay") == null ? null : (String)map.get("creWay");
        String sTime = map.get("sTime") == null ? null : (String)map.get("sTime");
        String eTime = map.get("eTime") == null ? null : (String)map.get("eTime");

        if (StringUtils.isBlank(creWay)){
            logger.debug("必要参数缺失");
            return false;
        }
        //按日期生成日结ALL
        if (StringUtils.equalsIgnoreCase("ALL", creWay)){
            if (StringUtils.isBlank(sTime)
                    || StringUtils.isBlank(eTime)){
                logger.debug("日期参数缺失");
                return false;
            }
            map.put("sTime", sTime.trim());
            map.put("eTime", eTime.trim());
        }

        return true;
    }

    /**
     * 为ALL时，结算时间限制
     * 1.eTime(天) < 当前时间(天)
     * 2.sTime(月) <= eTime(月) - 1
     * 3.sTime <= eTime
     * @return
     */
    private int checkTime(Map<String, Object> map){

        String sTime = (String)map.get("sTime");
        String eTime = (String)map.get("eTime");
        String cTime = DateUtil.format(DateUtil.date(),"yyyy-MM-dd");

        //1.eTime(天) < 当前时间(天)
        if (eTime.compareTo(cTime) >= 0){
            logger.debug("结束日期必须小于当前日期");
            return 1;
        }

        //2.sTime(月) <= eTime(月) - 1
        String sTimeMon = DateUtil.format(DateUtil.parseDate(sTime),"yyyy-MM");
        String eTimeMon = DateUtil.format(DateUtil.offsetMonth(DateUtil.parseDate(eTime),-1),"yyyy-MM");
        if (sTimeMon.compareTo(eTimeMon) > 0){
            logger.debug("起始日期必须小于结束日期，且月份至少间隔1个月");
            return 2;
        }

        //3.sTime <= eTime
        /*if (sTime.compareTo(eTime) > 0){
            logger.debug("起始日期不能大于结束日期");
            return 3;
        }*/

        return 0;  //无问题
    }

}

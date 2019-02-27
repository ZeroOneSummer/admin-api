package com.dms.api.modules.controller.report.mallrun;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.dms.api.common.utils.ParamMapUtils;
import com.dms.api.common.utils.R;
import com.dms.api.common.utils.StringUtils;
import com.dms.api.modules.base.AbstractController;
import com.dms.api.modules.entity.report.mallrun.MallRunEntity;
import com.dms.api.modules.service.report.mallrun.MallRunService;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/mallRun")
public class MallRunController extends AbstractController {

    @Autowired
    private MallRunService mallRunService;

    /**
     * 商城运营统计查询
     * @return
     */
    @RequestMapping("/list")
    @RequiresPermissions("mallRun:list")
    public R createSettlementData(@RequestBody Map<String, Object> map){

        logger.info("mallRun/list查询列表入参：{}", map);

        //参数校验
        String drawType = map.get("drawType") == null ? null : (String) map.get("drawType");
        String countType = map.get("countType") == null ? null : (String) map.get("countType");
        String dataType = map.get("dataType") == null ? null : (String) map.get("dataType");
        Integer dealerId = map.get("dealerId") == null ? null : (Integer) map.get("dealerId");
        if(countType == null
            || drawType == null
            || dataType == null
            || dealerId == null){
            logger.debug("必要参数缺失");
            return R.error("必要参数缺失");
        }

        //查询类型组合
        List<MallRunEntity> list = null;
        int result = this.selectQueryType(countType, dataType, dealerId);
        switch (result){
            case 1: list = mallRunService.mallRunCount1(map);break;
            case 2: list = mallRunService.mallRunCount2(map);break;
            case 3: list = mallRunService.mallRunCount3(map);break;
            case 4: list = mallRunService.mallRunCount4(map);break;
            case 5: list = mallRunService.mallRunCount5(map);break;
            case 6:
                String year = ((String)map.get("year")).trim();
                String month = ((String)map.get("month")).trim();
                map.put("qTime", year+"-"+month);
                list = mallRunService.mallRunCount6(map);
                break;
            default: break;
        }

        //drawType【Chart、Line】如是Chart表格类型需转换数据格式
        if (StringUtils.equalsIgnoreCase("Chart", drawType)){
            if (list != null){
                return R.ok("SUCCESS").put("list", this.dataProcess(list, countType, dataType));
            }
        }

        return R.ok("SUCCESS").put("list", list);
    }

    @RequestMapping("/download")
    @RequiresPermissions("mallRun:download")
    public R download(@RequestParam Map<String, Object> map, HttpServletResponse response) throws Exception {
        //查询列表数据
        logger.debug("mallRun/download导出列表入参：{}", map);

        //参数校验
        String countType = map.get("countType") == null ? null : (String) map.get("countType");
        String dataType = map.get("dataType") == null ? null : (String) map.get("dataType");
        Integer dealerId = map.get("dealerId") == null ? null : Integer.valueOf((String)map.get("dealerId"));
        //查询类型组合
        List<MallRunEntity> list = new ArrayList <>();
        String reporttName = "";
        int result = this.selectQueryType(countType, dataType, dealerId);
        switch (result){
            case 1: list = mallRunService.mallRunCount1(map);reporttName="按年统计单个服务商";break;
            case 2: list = mallRunService.mallRunCount2(map);reporttName="按年统计所有服务商";break;
            case 3: list = mallRunService.mallRunCount3(map);reporttName="按月统计单个服务商单日";break;
            case 4: list = mallRunService.mallRunCount4(map);reporttName="按月统计单个服务商累计";break;
            case 5: list = mallRunService.mallRunCount5(map);reporttName="按月统计所有服务商单日";break;
            case 6:
                String year = ((String)map.get("year")).trim();
                String month = ((String)map.get("month")).trim();
                map.put("qTime", year+"-"+month);
                list = mallRunService.mallRunCount6(map);
                reporttName="按月统计所有服务商累计";
                break;
            default: reporttName="未知查询类型";break;
        }

        if (list != null && list.size() > 0) {
            list.stream().forEach(entity -> {
                if (!StringUtils.equalsIgnoreCase(entity.getType(), "HZ")){
                    if(entity.getPrice() != null) entity.setPrice(entity.getPrice().abs()); //取正
                    if(entity.getAccumPrice() != null) entity.setAccumPrice(entity.getAccumPrice().abs()); //取正
                }
                entity.setType(ParamMapUtils.mallRunQueryTypeMap.get(entity.getType()));  //文字替换代码
            });
        }

        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), MallRunEntity.class, list);

        response.setHeader("content-Type", "application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 下载文件的默认名称
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(("商城运营查询-"+reporttName).getBytes("utf-8"), "iso8859-1") + ".xls");
        workbook.write(response.getOutputStream());

        return R.ok();
    }

    /**
     * 查询服务商列表
     * @return
     */
    @RequestMapping("/queryDealers")
    public R queryDealerList(@RequestParam Map<String, Object> map){

        logger.info("mallRun/queryDealers查询列表入参：{}", map);
        return R.ok("SUCCESS").put("list", mallRunService.queryDealerList(map));
    }

    /**
     * 查询类型筛选
     * @return
     */
    private int selectQueryType(String countType, String dataType , Integer dealerId){
        logger.info("开始查询类型筛选...");

        if(StringUtils.equalsIgnoreCase(countType,"Y") && dealerId != -1 && StringUtils.equalsIgnoreCase(dataType,"null")){ //1.按年统计单个服务商 countType='Y'，dealerId!=-1，dataType='null'
            logger.info(">>> 1.按年统计单个服务商 countType='Y'，dealerId!=-1，dataType='null'");
            return 1;
        }else if(StringUtils.equalsIgnoreCase(countType,"Y") && dealerId == -1 && StringUtils.equalsIgnoreCase(dataType,"null")){ //2.按年统计所有服务商 countType='Y'，dealerId=-1，dataType='null'
            logger.info(">>> 2.按年统计所有服务商 countType='Y'，dealerId=-1，dataType='null'");
            return 2;
        }else if(StringUtils.equalsIgnoreCase(countType,"M") && dealerId != -1 && StringUtils.equalsIgnoreCase(dataType,"SIG")){ //3.按月统计单个服务商单日 countType='M'，dealerId!=-1，dataType='SIG'
            logger.info(">>> 3.按月统计单个服务商单日 countType='M'，dealerId!=-1，dataType='SIG'");
            return 3;
        }else if(StringUtils.equalsIgnoreCase(countType,"M") && dealerId != -1 && StringUtils.equalsIgnoreCase(dataType,"ADD")){ //4.按月统计单个服务商累计 countType='M'，dealerId!=-1，dataType='ADD'
            logger.info(">>> 4.按月统计单个服务商累计 countType='M'，dealerId!=-1，dataType='ADD'");
            return 4;
        }else if(StringUtils.equalsIgnoreCase(countType,"M") && dealerId == -1 && StringUtils.equalsIgnoreCase(dataType,"SIG")){ //5.按月统计所有服务商单日 countType='M'，dealerId=-1，dataType='SIG'
            logger.info(">>> 5.按月统计所有服务商单日 countType='M'，dealerId=-1，dataType='SIG'");
            return 5;
        }else if(StringUtils.equalsIgnoreCase(countType,"M") && dealerId == -1 && StringUtils.equalsIgnoreCase(dataType,"ADD")){ //6.按月统计所有服务商累计 countType='M'，dealerId=-1，dataType='ADD'
            logger.info(">>> 6.按月统计所有服务商累计 countType='M'，dealerId=-1，dataType='ADD'");
            return 6;
        }else{
            logger.info(">>> 0.未匹配到任何查询类型");
            return 0;
        }
    }

    /**
     * drawType=Chart时表格数据加工
     * @return
     */
    private Object dataProcess(List<MallRunEntity> list, String countType, String dataType){

        //日、月数组
        String[] monArray = {"01","02","03","04","05","06","07","08","09","10","11","12"};
        String[] dayArray = {"01","02","03","04","05","06","07","08","09","10","11","12","13","14","15",
                             "16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};

        if(StringUtils.equalsIgnoreCase(countType,"Y")){//1.展示月countType="Y"
            Map<String, Object> map = new TreeMap<>();//默认按key升序
            //初始化map
            logger.info(">>> 初始化表格数据");
            for (int i=0; i<monArray.length; i++){
                map.put(monArray[i], 0);
            }
            //逐月匹配
            logger.info(">>> 按月匹配开始");
            list.stream().forEach(entity -> {
                for (int i=0; i<monArray.length; i++){
                    if(StringUtils.equalsIgnoreCase(StringUtils.substring(entity.getTraDay(),5,7), monArray[i])){
                        map.put(monArray[i], entity.getPrice());
                    }
                }
            });
            logger.info(">>> 按月匹配完成");

            //map转list
            return map;
        }else if(StringUtils.equalsIgnoreCase(countType,"M")){//2.展示日countType="M"
            Map<String, Object> map = new TreeMap <>();//默认按key升序
            //初始化map
            logger.info(">>> 初始化表格数据");
            Map<String, Object> map3 = new HashMap<>();
            map3.put("price", 0);
            map3.put("accumPrice", 0);
            for (int i=0; i<dayArray.length; i++){
                map.put(dayArray[i], map3);
            }
            //逐日匹配
            logger.info(">>> 按日匹配开始");
            list.stream().forEach(entity -> {
                for (int i=0; i<dayArray.length; i++){
                    if(StringUtils.equalsIgnoreCase(StringUtils.substring(entity.getTraDay(),8,10), dayArray[i])){
                        Map<String, Object> map2 = new HashMap<>();
                        map2.put("price", entity.getPrice());
                        map2.put("accumPrice", entity.getAccumPrice());
                        map.put(dayArray[i], map2);
                    }
                }
            });
            logger.info(">>> 按日匹配完成");

            //map转list
            return map.entrySet().stream().map(et -> et.getValue()).collect(Collectors.toList());
        }else{//3.无匹配类型
            logger.info(">>> 无匹配类型");
            return null;
        }

    }

}

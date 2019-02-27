package com.dms.api.modules.controller.reserve.shiftorg;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.dms.api.common.utils.*;
import com.dms.api.modules.entity.reserve.shiftorg.ShiftOrgRecordEntity;
import com.dms.api.modules.entity.reserve.shiftorg.ShiftOrgVerifyEntity;
import com.dms.api.modules.entity.reserve.shiftorg.ShiftOrgVerifyEntityForMac;
import com.dms.api.modules.service.reserve.shiftorg.ShiftOrgRecordService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 40
 * date 2018/1/9
 * time 9:40
 * decription:用户转移机构
 */
@Controller
@RequestMapping("org/shiftOrg")
public class ShiftOrgController {

    Logger log = LoggerFactory.getLogger(ShiftOrgController.class);

    @Autowired
    private ShiftOrgRecordService shiftOrgRecordService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("shiftorgrecord:list")
    @ResponseBody
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        List<ShiftOrgRecordEntity> list = shiftOrgRecordService.queryList(query);
        int total = shiftOrgRecordService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(list, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }


    /**
     * 下载转移记录
     *
     * @param response
     * @throws Exception
     */
    @RequestMapping("/downRecord")
    @RequiresPermissions("shiftorgrecord:downRecord")
    public void downRecord(HttpServletResponse response, @RequestParam Map<String, Object> params) throws Exception {

        List<ShiftOrgRecordEntity> list = shiftOrgRecordService.queryList(params);
        list.forEach(entity -> {
                    String statusSt;
                    switch (entity.getStatus()) {
                        case 0:
                            statusSt = "待处理";
                            break;
                        case -1:
                            statusSt = "失败";
                            break;
                        case 1:
                            statusSt = "成功";
                            break;
                        case 2:
                            statusSt = "处理中";
                            break;
                        default:
                            statusSt = "未知状态";
                    }
                    entity.setStatusSt(statusSt);
                }
        );
        ExportParams exportParams = new ExportParams();
        exportParams.setTitle("机构转移记录");
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, ShiftOrgRecordEntity.class, list);
        response.setHeader("content-Type", "application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 下载文件的默认名称
        response.setHeader("Content-Disposition", "attachment;filename=" + TimeUtil.getCurrentday() + new String("_机构转移记录".getBytes("utf-8"), "iso8859-1") + ".xls");
        workbook.write(response.getOutputStream());
    }

    /**
     * 转机构审核列表
     */
    @RequestMapping("/verifyList")
    @RequiresPermissions("shiftorgrecord:verify:list")
    @ResponseBody
    public R verifyList(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        List<ShiftOrgVerifyEntity> shiftOrgRecordList = shiftOrgRecordService.queryVerifyList(query);
        int total = shiftOrgRecordService.queryVerifyTotal(query);

        PageUtils pageUtil = new PageUtils(shiftOrgRecordList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 下载审核记录
     *
     * @param response
     * @throws Exception
     */
    @RequestMapping("/downVerify")
    @RequiresPermissions("shiftorgrecord:downVerify")
    public void downVerify(HttpServletResponse response, @RequestParam Map<String, Object> params) throws Exception {

        List<ShiftOrgVerifyEntity> shiftOrgRecordList = shiftOrgRecordService.queryVerifyList(params);
        shiftOrgRecordList.forEach(entity -> {
                    String statusSt;
                    switch (entity.getStatus()) {
                        case 0:
                            statusSt = "审核中";
                            break;
                        case -1:
                            statusSt = "驳回";
                            break;
                        case 99:
                            statusSt = "通过";
                            break;
                        default:
                            statusSt = "未知结果";
                    }
                    entity.setStatusSt(statusSt);
                }
        );
        ExportParams exportParams = new ExportParams();
        exportParams.setTitle("机构审核记录");
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, ShiftOrgVerifyEntity.class, shiftOrgRecordList);
        //去除模板不必要的字段
        int[] noNeed = {1, 5};
        for (int index : noNeed) {
            workbook.getSheetAt(0).setColumnHidden(index, true);
        }
        response.setHeader("content-Type", "application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 下载文件的默认名称
        response.setHeader("Content-Disposition", "attachment;filename=" + TimeUtil.getCurrentday() + new String("_机构审核记录".getBytes("utf-8"), "iso8859-1") + ".xls");
        workbook.write(response.getOutputStream());
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("shiftorgrecord:info")
    @ResponseBody
    public R info(@PathVariable("id") Long id) {
        ShiftOrgRecordEntity shiftOrgRecord = shiftOrgRecordService.queryObject(id);

        return R.ok().put("shiftOrgRecord", shiftOrgRecord);
    }

    /**
     * 批量审核
     */
    @RequestMapping("/verifyBatch")
    @RequiresPermissions("shiftorgrecord:verifyBatch")
    @ResponseBody
    public R verify(@RequestBody Map<String, Object> params) {
        shiftOrgRecordService.verifyBatch(params);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("shiftorgrecord:delete")
    @ResponseBody
    public R delete(@RequestBody Long[] ids) {
        shiftOrgRecordService.deleteBatch(ids);

        return R.ok();
    }


    /**
     * 在待处理的任务情况下 驳回
     */
    @RequestMapping("/reBack")
    @RequiresPermissions("shiftorgrecord:reBack")
    @ResponseBody
    public R reBack(@RequestBody Long id) {
        shiftOrgRecordService.reBack(id);
        return R.ok();
    }

    /**
     * 导入名单
     *
     * @param file
     * @param request
     * @return
     */
    @RequestMapping(value = "/import", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions("shiftorgrecord:import")
    public R importExcel(MultipartFile file, HttpServletRequest request) {
        try {
            ImportParams params = new ImportParams();
            params.setTitleRows(1);
            params.setHeadRows(1);
            params.setNeedSave(true);
            String path = request.getSession().getServletContext().getRealPath("");
            String filename = path + "/excel/" + file.getOriginalFilename();
            File f = new File(filename);
            if (!f.exists()) {
                String fileName = file.getOriginalFilename();
                if (!ExcelImportUtils.validateExcel(fileName)) {
                    return R.error("请上传EXCEL文件！");
                }
                try {
                    File dir = new File(path + "/excel/");
                    dir.mkdirs();
                    if (!f.createNewFile()) {
                        return R.error("系统异常！请联系管理员");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return R.error("系统异常！请联系管理员");
                }
            }
            file.transferTo(f);


            List<ShiftOrgVerifyEntity> list = read(f,isExcel2003(filename));//ExcelImportUtil.importExcel(f, ShiftOrgVerifyEntity.class, params);
            if (list.size() > 0) {
                if (list.size() <= 500){
                    return shiftOrgRecordService.importFromExcel(list);
                }else {
                    return R.error("导入名单超过500上限，请分批导入！");
                }

            } else {
                return R.error("导入名单有误，请检查！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("系统异常！请联系管理员");
        }
    }

    /**
     * 下载模板
     *
     * @param response
     * @throws Exception
     */
    @RequestMapping("/download")
    @RequiresPermissions("shiftorgrecord:template:download")
    public void download(HttpServletResponse response) throws Exception {
        String dateSt = DateUtils.format(new Date());
        ExportParams params = new ExportParams();
        params.setTitle(dateSt + "_机构转移名单");
        Workbook workbook = ExcelExportUtil.exportExcel(params, ShiftOrgVerifyEntityForMac.class, new ArrayList<>());
//        //去除模板不必要的字段
//        int[] noNeed = {1, 5, 6, 7, 8, 9}; //表格下标
//        for (int index : noNeed) {
//            System.out.println(index);
//            workbook.getSheetAt(0).setColumnHidden(index, true);
//        }
        response.setHeader("content-Type", "application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 下载文件的默认名称
        response.setHeader("Content-Disposition", "attachment;filename=" + new String("机构转移名单模板".getBytes("utf-8"), "iso8859-1") + ".xls");
        workbook.write(response.getOutputStream());
    }


    /**
     * 读取excel文件
     * @param f
     * @param isExcel2003 是否为2003版本，文件兼容性
     * @return
     * @throws IOException
     */
    private List<ShiftOrgVerifyEntity> read(File f,boolean isExcel2003) throws IOException {
        List<ShiftOrgVerifyEntity> list = null;
        FileInputStream in = new FileInputStream(f);
        Workbook workbook = null;
        if (isExcel2003) {
            workbook = new HSSFWorkbook(in);
        } else {
            workbook = new XSSFWorkbook(in);
        }
//        HSSFWorkbook workbook = new HSSFWorkbook(in);
        try {
        Sheet sheet = null;
        int i = workbook.getSheetIndex("Sheet0"); // sheet表名
        sheet = workbook.getSheetAt(i);
        list = new ArrayList<>(sheet.getLastRowNum());
        for (int j = 2; j < sheet.getLastRowNum() + 1; j++) {// getLastRowNum
            // 获取最后一行的行标
            ShiftOrgVerifyEntity stemp = new ShiftOrgVerifyEntity();
            Row row = sheet.getRow(j);
            if (row != null) {
                for (int  k= 0; k < row.getLastCellNum(); k++) {// getLastCellNum
                    // 是获取最后一个不为空的列是第几个
                    if (row.getCell(k) != null) { // getCell 获取单元格数据

                        if (k == 0){
                            if (row.getCell(k) .getCellType() == HSSFCell.CELL_TYPE_NUMERIC){
                                DecimalFormat df = new DecimalFormat("#");

                                stemp.setUserName(df.format(row.getCell(k).getNumericCellValue()));
                            } else {
                                stemp.setUserName(row.getCell(k).getStringCellValue());
                            }

                        }

                        if (k == 1){
                            if (row.getCell(k) .getCellType() == HSSFCell.CELL_TYPE_NUMERIC){
                                DecimalFormat df = new DecimalFormat("#");

                                stemp.setLoginCode(df.format(row.getCell(k).getNumericCellValue()));
                            } else {
                                stemp.setLoginCode(row.getCell(k).getStringCellValue());
                            }

                        }

                        if (k == 2){
                            if (row.getCell(k) .getCellType() == HSSFCell.CELL_TYPE_NUMERIC){
                                DecimalFormat df = new DecimalFormat("#");

                                stemp.setSrcOrgCode(df.format(row.getCell(k).getNumericCellValue()));
                            } else {
                                stemp.setSrcOrgCode(row.getCell(k).getStringCellValue());
                            }

                        }

                        if (k == 3){
                            if (row.getCell(k) .getCellType() == HSSFCell.CELL_TYPE_NUMERIC){
                                DecimalFormat df = new DecimalFormat("#");

                                stemp.setTarOrgCode(df.format(row.getCell(k).getNumericCellValue()));
                            } else {
                                stemp.setTarOrgCode(row.getCell(k).getStringCellValue());
                            }

                        }

                    } else {
                    }
                }
                if ( ! (StringUtils.isBlank(stemp.getUserName()) && StringUtils.isBlank(stemp.getLoginCode())
                        && StringUtils.isBlank(stemp.getSrcOrgCode()) && StringUtils.isBlank(stemp.getTarOrgCode()))){
                    list.add(stemp);
                }

            }

        }
        } catch (Exception e){
            log.error(e.getMessage(),e);
        }finally {
            in.close();
            workbook.close();
        }

        return list;
    }


    /**
     * @描述：是否是2003的excel，返回true是2003
     * @时间：2014-08-29 下午16:29:11
     * @参数：@param filePath　文件完整路径
     * @参数：@return
     * @返回值：boolean
     */
    public static boolean isExcel2003(String filePath) {
        return filePath.matches("^.+\\.(?i)(xls)$");
    }

    /**
     * @描述：是否是2007的excel，返回true是2007
     * @时间：2014-08-29 下午16:28:20
     * @参数：@param filePath　文件完整路径
     * @参数：@return
     * @返回值：boolean
     */
    public static boolean isExcel2007(String filePath) {
        return filePath.matches("^.+\\.(?i)(xlsx)$");
    }
}

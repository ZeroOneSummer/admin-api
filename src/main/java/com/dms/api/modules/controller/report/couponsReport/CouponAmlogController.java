package com.dms.api.modules.controller.report.couponsReport;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.dms.api.common.utils.PageUtils;
import com.dms.api.common.utils.ParamMapUtils;
import com.dms.api.common.utils.Query;
import com.dms.api.common.utils.R;
import com.dms.api.modules.base.AbstractController;
import com.dms.api.modules.entity.report.couponsReport.CouponAmlog;
import com.dms.api.modules.entity.sys.user.SysUserEntity;
import com.dms.api.modules.service.report.couponsReport.CouponAmlogService;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author jp
 * date 2018/08/22 11:02
 * decription: 券流水报表
 */
@RequestMapping("couponsReport/couponAmountlog")
@RestController
public class CouponAmlogController extends AbstractController {

    @Autowired
    CouponAmlogService couponAmountlogService;

    @RequestMapping("/list")
    @RequiresPermissions("couponsReport:couponAmountlog:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        SysUserEntity user = getUser();
        if (!user.isSuperAdmin()) {
            query.put("dealerId", user.getDealerId());
        }

        List<CouponAmlog> list = couponAmountlogService.queryList(query);
        int total = couponAmountlogService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(list, total, query);
        return R.ok().put("page", pageUtil);
    }


    @RequestMapping("/download")
    @RequiresPermissions("couponsReport:couponAmountlog:download")
    public R download(@RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        //获取查询
        Query query = new Query(params);
        //添加权限
        SysUserEntity user = getUser();
        if (!user.isSuperAdmin()) {
            query.put("dealerId", user.getDealerId());
        }

        if(query == null){
            query = new Query();
        }else {
            //移除分页
            query.remove("page");
            query.remove("limit");
        }

        List<CouponAmlog> list = couponAmountlogService.queryList(query);
        if (list != null && list.size() > 0) {
            list.stream().forEach(couponAmountlog -> {
                couponAmountlog.setType(ParamMapUtils.couponsLogTypeMap.get(couponAmountlog.getType()));  //文字替换代码
            });
        }
        if (list == null){ list = new ArrayList<>(); }
        int total = couponAmountlogService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(list, total, query);

        //报表参数对象
        ExportParams exportParams = new ExportParams();
        String[] exclu = {}; //忽略字段
        //报表名称
        String reportStr = "券流水查询";
        exportParams.setExclusions(exclu);

        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, CouponAmlog.class, list);

        response.setHeader("content-Type", "application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 下载文件的默认名称
        String dataStr = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        response.setHeader("Content-Disposition", "attachment;filename="+new String((dataStr + reportStr).getBytes("utf-8"), "iso8859-1")+".xls");
        workbook.write(response.getOutputStream());

        return R.ok().put("page", pageUtil);
    }

}

package com.dms.api.modules.controller.report.couponsReport;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.dms.api.common.utils.PageUtils;
import com.dms.api.common.utils.ParamMapUtils;
import com.dms.api.common.utils.Query;
import com.dms.api.common.utils.R;
import com.dms.api.modules.base.AbstractController;
import com.dms.api.modules.entity.report.couponsReport.CouponOrder;
import com.dms.api.modules.entity.sys.user.SysUserEntity;
import com.dms.api.modules.service.report.couponsReport.CouponOrderService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
 * date 2018/08/28 10:02
 * decription: 券下单报表
 */
@RequestMapping("couponsReport/couponOrder")
@RestController
public class CouponOrderController extends AbstractController {

    @Autowired
    CouponOrderService couponOrderService;

    @RequestMapping("/list")
    @RequiresPermissions("couponsReport:couponOrder:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        SysUserEntity user = getUser();
        if (!user.isSuperAdmin()) {
            query.put("dealerId", user.getDealerId());
        }

        int page = (int)query.get("page"); //页码
        int limit = query.getLimit(); //页面容量
        PageHelper.startPage(page, limit);
        List<CouponOrder> list = couponOrderService.queryList(query);
        PageInfo<CouponOrder> pageInfo = new PageInfo(list);
        int total = (int) pageInfo.getTotal();

        PageUtils pageUtil = new PageUtils(list, total, query);
        return R.ok().put("page", pageUtil);
    }


    @RequestMapping("/download")
    @RequiresPermissions("couponsReport:couponOrder:download")
    public R download(@RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        //获取查询
        Query query = new Query(params);
        //添加权限
        SysUserEntity user = getUser();
        if (!user.isSuperAdmin()) {
            query.put("dealerId", user.getDealerId());
        }

        if(query == null) query = new Query();

        List<CouponOrder> list = couponOrderService.queryList(query);
        PageInfo<CouponOrder> pageInfo = new PageInfo(list);
        int total = (int) pageInfo.getTotal();

        if (list != null && list.size() > 0) {
            list.stream().forEach(couponOrder -> {
                couponOrder.setCouponType(ParamMapUtils.couponOrderTypeMap.get(couponOrder.getCouponType()));  //文字替换代码
            });
        }
        if (list == null){ list = new ArrayList<>(); }

        PageUtils pageUtil = new PageUtils(list, total, query);

        //报表参数对象
        ExportParams exportParams = new ExportParams();
        String[] exclu = {}; //忽略字段
        //报表名称
        String reportStr = "券订单查询";
        exportParams.setExclusions(exclu);

        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, CouponOrder.class, list);

        response.setHeader("content-Type", "application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 下载文件的默认名称
        String dataStr = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        response.setHeader("Content-Disposition", "attachment;filename="+new String((dataStr + reportStr).getBytes("utf-8"), "iso8859-1")+".xls");
        workbook.write(response.getOutputStream());

        return R.ok().put("page", pageUtil);
    }

}

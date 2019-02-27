package com.dms.api.modules.controller.report.bean;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import com.dms.api.common.utils.PageUtils;
import com.dms.api.common.utils.Query;
import com.dms.api.common.utils.R;
import com.dms.api.common.utils.StringUtils;
import com.dms.api.core.config.CertificateConfig;
import com.dms.api.core.util.ExportExcelUtils;
import com.dms.api.modules.base.AbstractController;
import com.dms.api.modules.entity.report.bean.DealerCount;
import com.dms.api.modules.entity.report.bean.OrgCount;
import com.dms.api.modules.entity.report.bean.SingleConsumeDetail;
import com.dms.api.modules.entity.report.bean.SingleCount;
import com.dms.api.modules.entity.sys.user.SysUserEntity;
import com.dms.api.modules.service.report.bean.HxBountyService;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 奖励金_资金流水表
 */
@RestController
@RequestMapping("hxbounty")
public class HxBountyController extends AbstractController {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private HxBountyService hxBountyService;

//    @Value("${systema.url}")
    private String url;

    @Value("${templatePath}")
    private String templatePath;

    /**
     * 列表
     */
    @RequestMapping("/count")
//	@RequiresPermissions("hxbounty:list")
    public R list(@RequestParam Map<String, Object> params) {
        Map<String, Object> countBounty = new HashMap<>();
        //查询列表数据
        SysUserEntity user = getUser();
        countBounty = hxBountyService.countBounty(user);

        return R.ok().put("data", countBounty);
    }

    @RequestMapping("/dealerCount")
    public R dealerCount(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        SysUserEntity user = getUser();
        query.put("user", user);
        List<DealerCount> dealerList = hxBountyService.countDealerBounty(query);
        int total = 0;
        query.put("dealerId", user.getDealerId());
        total = hxBountyService.dealerTotal(query);

        //查询列表数据

        PageUtils pageUtil = new PageUtils(dealerList, total, query);

        return R.ok().put("page", pageUtil);
    }

    @RequestMapping("/orgCount")
    public R orgCount(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        SysUserEntity user = getUser();
        query.put("user", user);
        //查询列表数据
        List<OrgCount> orgList = hxBountyService.countOrgBounty(query);
        int total = hxBountyService.getOrgsSizeByUser(user, query);
        PageUtils pageUtil = new PageUtils(orgList, total, query);

        return R.ok().put("page", pageUtil);
    }

    @RequestMapping("/singleCount")
    public R singleCount(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        SysUserEntity user = getUser();
        query.put("user", user);
        List<SingleCount> singleList;
        int total = 0;
        singleList = hxBountyService.countSingleBounty(query);
        total = hxBountyService.singleTotal(user, query);
        PageUtils pageUtil = new PageUtils(singleList, total, query);
        return R.ok().put("page", pageUtil);
    }

    @RequestMapping("/ConsumeDetail")
    public R ConsumeDetail(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        query.put("sidx", StringUtils.camelToUnderline(query.get("sidx") + ""));
        //查询列表数据
        List<SingleConsumeDetail> singleDetailList = hxBountyService.ConsumeDetail(query);

        int total = hxBountyService.ConsumeDetailTotal(query);
        PageUtils pageUtil = new PageUtils(singleDetailList, total, query);
        return R.ok().put("page", pageUtil);
    }

    // 下载execl文档
    @RequestMapping("/download")
    public void download(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> requestParams) throws Exception {
        //金豆名称
        String beanName = CertificateConfig.beanName;

        String type = request.getParameter("type");
        String fileName = "";
        List<?> list = new ArrayList <>();
        Workbook workbook = null;
        Query query = new Query(requestParams);
        //去除导出数据分页
        query.remove("limit");
        query.remove("offset");

        //实体与表头关联
        LinkedHashMap<String, String> formMap = new LinkedHashMap<>();
        if ("org".equals(type)) {
            String orgCode = request.getParameter("orgCode");
            SysUserEntity user = getUser();
            query.put("orgCode", orgCode);
            query.put("user", user);
            fileName += "机构"+beanName+"信息";
            list = hxBountyService.countOrgBounty(query);

            formMap.put("orgCode", "机构编码");
            formMap.put("orgName", "机构名称");
            formMap.put("total", "收入"+beanName+"总额");
            formMap.put("consume", "支出"+beanName+"总额");
            formMap.put("invalid", "失效"+beanName+"总额");
            formMap.put("usable", "当前可用"+beanName+"总额");
            formMap.put("morrow", "15日内即将过期"+beanName);
        } else if ("single".equals(type)) {
            fileName += "个人"+beanName+"信息";
            SysUserEntity user = getUser();
            query.put("user", user);
            list = hxBountyService.countSingleBounty(query);

            formMap.put("loginCode", "客户登录账号");
            formMap.put("userMobile", "手机号码");
            formMap.put("dealerCode", "所属服务商");
            formMap.put("orgCode", "所属机构");
            formMap.put("total", "收入"+beanName+"总额");
            formMap.put("consume", "支出"+beanName+"总额");
            formMap.put("invalid", "失效"+beanName+"总额");
            formMap.put("usable", "当前可用"+beanName+"总额");
            formMap.put("currentEndTime", "账单日");
            formMap.put("willExpire", "15日内即将过期"+beanName+"");
        } else if ("ConsumeDetail".equals(type)) {
            fileName += "个人"+beanName+"明细信息";
            //查询列表数据
            query.put("type", request.getParameter("qType"));
            query.put("orderNo", request.getParameter("orderNo"));
            list = hxBountyService.ConsumeDetail(query);
            for (Object s : list) {
                SingleConsumeDetail ss = (SingleConsumeDetail)s;
                String typeSt = "";
                if (ss.getType()!=null) {
                    switch (ss.getType()) {
                        case "B010":
                            typeSt = "收入";
                            break;
                        case "B001":
                            typeSt = "抵扣货款(全款)";
                            break;
                        case "B002":
                            typeSt = "抵扣货款(预付款)";
                            break;
                        case "B003":
                            typeSt = "过期扣除";
                            break;
                        case "B004":
                            typeSt = "全款单"+beanName+"退全款";
                            break;
                        case "B006":
                            typeSt = "全款单"+beanName+"退差价";
                            break;
                        case "B005":
                            typeSt = "抽奖";
                            break;
                        case "B101":
                            typeSt = "红冲";
                            break;
                        case "B102":
                            typeSt = "蓝补";
                            break;
                        case "B103":
                            typeSt = "核销";
                            break;
                        default:

                    }
                    ss.setType(typeSt);
                }
            }

            formMap.put("nickName", "客户姓名");
            formMap.put("type", ""+beanName+"类型");
            formMap.put("total", "使用数量");
            formMap.put("date", "时间");
            formMap.put("orderNo", "订单号");
            formMap.put("orderType", "订单类型");
            formMap.put("skuInfo", "产品类型");
            formMap.put("orderMoney", "订单金额");
            formMap.put("payment", "实付金额");
            formMap.put("refundDiff", "全款单"+beanName+"退差价");
        } else if ("dealer".equals(type)) {
            //根据模板导出
            TemplateExportParams params = new TemplateExportParams(templatePath);
            logger.info(templatePath);
            fileName += "服务商"+beanName+"信息";
            SysUserEntity user = getUser();
            query.put("user", user);
            Map<String, Object> map = hxBountyService.countBounty(user);
            List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();

            List<DealerCount> dealerList = hxBountyService.countDealerBounty(query);
            for (DealerCount d : dealerList) {
                Map<String, String> lm = new HashMap<String, String>();
                lm.put("dealerCode", d.getDealerCode());
                lm.put("total", d.getTotal() == null ? "" : d.getTotal() + "");
                lm.put("usable", d.getUsable() == null ? "" : d.getUsable() + "");
                lm.put("consume", d.getConsume() == null ? "" : d.getConsume() + "");
                lm.put("invalid", d.getInvalid() == null ? "" : d.getInvalid() + "");
                lm.put("morrow", d.getMorrow() == null ? "" : d.getMorrow() + "");
                listMap.add(lm);
            }
            map.put("maplist", listMap);

            workbook = ExcelExportUtil.exportExcel(params, map);
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("utf-8"), "ISO-8859-1") + ".xls");
            workbook.write(response.getOutputStream());
        }

        //用新方法导出
        if(!"dealer".equals(type)){
            ExportExcelUtils.export(fileName, list, formMap, response);
        }
    }

}

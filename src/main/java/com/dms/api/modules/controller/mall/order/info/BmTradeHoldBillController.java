package com.dms.api.modules.controller.mall.order.info;

import com.dms.api.common.Enum.ChartType;
import com.dms.api.common.exception.DMException;
import com.dms.api.common.utils.PageUtils;
import com.dms.api.common.utils.Query;
import com.dms.api.common.utils.R;
import com.dms.api.common.utils.StringUtils;
import com.dms.api.modules.base.AbstractController;
import com.dms.api.modules.entity.mall.order.info.BmTradeHoldBill;
import com.dms.api.modules.entity.sys.user.SysUserEntity;
import com.dms.api.modules.service.mall.order.info.BmTradeHoldBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * BmTradeHoldBillController
 */
@RequestMapping("/dataMonitor/bmTradeHoldBill")
@Controller
public class BmTradeHoldBillController extends AbstractController {

    @Autowired
    BmTradeHoldBillService bmTradeHoldBillService;

    @ResponseBody
    @RequestMapping("/list")
//  @RequiresPermissions("org:dealer:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        query.put("sidx", StringUtils.camelToUnderline(query.get("sidx") + ""));
        SysUserEntity user = getUser();
        if (!user.isSuperAdmin()) {
            query.put("dealerId", user.getDealerId());
        }
        List<BmTradeHoldBill> bmTradeHoldBills = bmTradeHoldBillService.queryList(query);
        int total = bmTradeHoldBillService.queryTotal(query);
        PageUtils pageUtil = new PageUtils(bmTradeHoldBills, total, query.getLimit(), query.getPage());
        return R.ok().put("page", pageUtil);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
//    @RequiresPermissions("org:dealer:info")
    @ResponseBody
    public R info(@PathVariable("id") Long id) {
        BmTradeHoldBill bmTradeHoldBill = bmTradeHoldBillService.queryObject(id);

        return R.ok().put("bmTradeHoldBill", bmTradeHoldBill);
    }

    /**
     * 预订历史数据
     */
    @RequestMapping("/historyData/{dealerId}")
    @ResponseBody
    public R historyData(@PathVariable("dealerId") String dealerIdSt, @RequestBody Map<String, Object> params) {
        try {
            params = validateParam(dealerIdSt, params);
        } catch (DMException e) {
            return R.error(e.getMsg());
        }
        Map<String, Object> resMap = bmTradeHoldBillService.historyData(params);

        return R.ok().put("data", resMap);
    }

    /**
     * 历史下单分布情况
     */
    @RequestMapping("/historyDistribution")
    @ResponseBody
    public R historyDistribution(@RequestBody Map<String, Object> params) {
        //sort:v("money","order_count")
        SysUserEntity user = getUser();
        if (!user.isSuperAdmin()) {
            params.put("dealerId", user.getDealerId());
        }
        return bmTradeHoldBillService.historyDistribution(params);
    }

    /**
     * 校验查询条件是否有误
     *
     * @param dealerIdSt
     * @param params
     * @return
     */
    private Map<String, Object> validateParam(String dealerIdSt, Map<String, Object> params) {
        ChartType chartType = ChartType.getChartTypeByName(params.get("chartType") + "");
        if (null == chartType || (!"WEIGHT_TYPE".equals(chartType.getType()) && !"PREPAY_TYPE".equals(chartType.getType()))) {
            throw new DMException("查询条件有误");
        }
        params.put("chartType", chartType);
        Long dealerId = "null".equals(dealerIdSt) ? null : Long.parseLong(dealerIdSt);
        SysUserEntity user = getUser();
        if (!user.isSuperAdmin()) {
            dealerId = user.getDealerId();
        }
        params.put("dealerId", dealerId);
        return params;
    }
}

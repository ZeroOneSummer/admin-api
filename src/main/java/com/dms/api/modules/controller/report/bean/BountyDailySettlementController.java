package com.dms.api.modules.controller.report.bean;

import com.dms.api.common.utils.PageUtils;
import com.dms.api.common.utils.Query;
import com.dms.api.common.utils.R;
import com.dms.api.core.config.CertificateConfig;
import com.dms.api.core.util.ExportExcelUtils;
import com.dms.api.modules.entity.report.bean.BountyDailySettlementEntity;
import com.dms.api.modules.service.report.bean.BountyDailySettlementService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务商/机构K豆日结表
 * @author 40
 * @email chenshiling@danpacmall.com
 * @date 2018-01-16 10:55:44
 */
@RestController
@RequestMapping("bountyDailySettlement")
public class BountyDailySettlementController {

    @Autowired
    private BountyDailySettlementService bountyDailySettlementService;

    /**
     * 服务商日结列表
     */
    @RequestMapping(value = "/dealerList/{dealerId}", method = RequestMethod.GET)
//    @RequiresPermissions("bountyDailySettlement:dealerList")
    public R dealerList(@RequestParam Map<String, Object> params, @PathVariable String dealerId) {
        if (!"".equals(dealerId) && null != dealerId) {
            params.put("dealerId", dealerId);
            params.put("orgId", null);
        }
        //查询列表数据
        Query query = new Query(params);

        List<BountyDailySettlementEntity> bountyDailySettlementList = bountyDailySettlementService.queryListByDealer(query);
        int total = bountyDailySettlementService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(bountyDailySettlementList, total,query);

        return R.ok().put("page", pageUtil);
    }

    @RequestMapping("/download/dealerList/{dealerId}")
//    @RequiresPermissions("bountyDailySettlement:dealerList:download")
    public void downloadDealer(@RequestParam Map<String, Object> params, @PathVariable String dealerId, HttpServletResponse response) throws Exception {
        if (!"".equals(dealerId) && null != dealerId) {
            params.put("dealerId", dealerId);
            params.put("orgId", null);
        }
        //查询列表数据
        Query query = new Query(params);
        query.remove("limit");
        query.remove("offset");
        List<BountyDailySettlementEntity> bountyDailySettlementList = bountyDailySettlementService.queryListByDealer(query);
        String dateSt = "服务商";

        //实体与表头关联
        String beanName = CertificateConfig.beanName;   //金豆名称
        String fileName = dateSt+beanName+"日结明细表";
        LinkedHashMap<String, String> formMap = new LinkedHashMap<>();
        formMap.put("tradingDay", "预订日");
        formMap.put("income", "收入总额");
        formMap.put("consume", "支出总额");
        formMap.put("invalid", "失效总额");
        ExportExcelUtils.export(fileName, bountyDailySettlementList, formMap, response);
    }

    /**
     * 机构日结列表
     */
    @RequestMapping(value = "/orgList/{orgId}", method = RequestMethod.GET)
//    @RequiresPermissions("bountyDailySettlement:orgList")
    public R orgList(@RequestParam Map<String, Object> params, @PathVariable String orgId) {
        if (!"".equals(orgId) && null != orgId) {
            params.put("dealerId", null);
            params.put("orgId", orgId);
        }
        //查询列表数据
        Query query = new Query(params);

        List<BountyDailySettlementEntity> bountyDailySettlementList = bountyDailySettlementService.queryListByOrg(query);
        int total = bountyDailySettlementService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(bountyDailySettlementList, total, query);

        return R.ok().put("page", pageUtil);
    }

    @RequestMapping("/download/orgList/{orgId}")
//    @RequiresPermissions("bountyDailySettlement:orgList:download")
    public void downloadOrg(@RequestParam Map<String, Object> params, HttpServletResponse response, @PathVariable String orgId) throws Exception {
        if (!"".equals(orgId) && null != orgId) {
            params.put("dealerId", null);
            params.put("orgId", orgId);
        }
        //查询列表数据
        Query query = new Query(params);
        query.remove("limit");
        query.remove("offset");
        List<BountyDailySettlementEntity> bountyDailySettlementList = bountyDailySettlementService.queryListByOrg(query);
        String dateSt = "机构";

        //实体与表头关联
        String beanName = CertificateConfig.beanName;   //金豆名称
        String fileName = dateSt+beanName+"日结明细表";
        LinkedHashMap<String, String> formMap = new LinkedHashMap<>();
        formMap.put("tradingDay", "预订日");
        formMap.put("income", "收入总额");
        formMap.put("consume", "支出总额");
        formMap.put("invalid", "失效总额");
        ExportExcelUtils.export(fileName, bountyDailySettlementList, formMap, response);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("bountyDailySettlement:info")
    public R info(@PathVariable("id") Long id) {
        BountyDailySettlementEntity bountyDailySettlement = bountyDailySettlementService.queryObject(id);

        return R.ok().put("bountyDailySettlement", bountyDailySettlement);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("bountyDailySettlement:save")
    public R save(@RequestBody BountyDailySettlementEntity bountyDailySettlement) {
        bountyDailySettlementService.save(bountyDailySettlement);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("bountyDailySettlement:update")
    public R update(@RequestBody BountyDailySettlementEntity bountyDailySettlement) {
        bountyDailySettlementService.update(bountyDailySettlement);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("bountyDailySettlement:delete")
    public R delete(@RequestBody Long[] ids) {
        bountyDailySettlementService.deleteBatch(ids);

        return R.ok();
    }

}

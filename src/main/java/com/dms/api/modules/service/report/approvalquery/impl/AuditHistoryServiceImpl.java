package com.dms.api.modules.service.report.approvalquery.impl;

import com.alibaba.fastjson.JSONObject;
import com.dms.api.common.utils.HttpClientUtil;
import com.dms.api.common.utils.PageUtils;
import com.dms.api.common.utils.R;
import com.dms.api.common.utils.StringUtils;
import com.dms.api.core.config.HostsConfig;
import com.dms.api.core.constants.ConstantTable;
import com.dms.api.modules.controller.report.approvalquery.AuditHistoryController;
import com.dms.api.modules.entity.report.approvalquery.AuditHistoryEntity;
import com.dms.api.modules.entity.report.approvalquery.AuditHistoryPageRespEntity;
import com.dms.api.modules.service.report.approvalquery.AuditHistoryService;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author: hejiaxi
 * @CreateDate: 2018/9/7 11:41
 * @Description: 作用描述
 */
@Service
public class AuditHistoryServiceImpl implements AuditHistoryService {
    Logger logger = LoggerFactory.getLogger(AuditHistoryController.class);

    @Override
    public R queryList(Map<String, String> Params) {

        String url = null;
        // getMyVacRecord 接口为我的申请的历史订单    getMyVacHistory 为我审批的历史订单查询
        if ( StringUtils.equals("applyOrder",Params.get("orderStatus"))) {
            url = HostsConfig.hostsAudit + ConstantTable.GET_VAC_RECORD;
        } else if (StringUtils.equals("auditOrder",Params.get("orderStatus"))) {
            url = HostsConfig.hostsAudit + ConstantTable.GET_VAC_HISTORY;
        } else {
            return R.error("订单查询类型为空!");
        }

        Map<String, String> map = Maps.newHashMap();
        map.put("processDefineKey", Params.get("queryType"));
        map.put("userName", Params.get("userName"));
        map.put("page", Params.get("page"));
        map.put("limit", Params.get("limit"));

        logger.info("请求url：{} 请求参数params：{}", url ,map);
        String response = HttpClientUtil.doGet(url, map, 3000);

        JSONObject jsonObject = JSONObject.parseObject(response);
        String data = jsonObject.getString("data");
        String msg = jsonObject.getString("msg");
        String rc = jsonObject.getString("rc");

        logger.info("接口返回数据"+data);
        if (!StringUtils.equalsIgnoreCase(rc, "200")) {
            return R.error("接口请求异常" + msg);
        }

        AuditHistoryPageRespEntity pageResp = JSONObject.parseObject(data, AuditHistoryPageRespEntity.class);
        List<AuditHistoryEntity> list = pageResp.getList();
        PageUtils pageutils = new PageUtils(list, Integer.parseInt(pageResp.getTotal()), Integer.parseInt(Params.get("limit")), Integer.parseInt(Params.get("page")));

        return R.ok().put("page", pageutils);
    }
}

package com.dms.api.modules.service.report.approvalquery.impl;

import com.alibaba.fastjson.JSONObject;
import com.dms.api.common.utils.HttpClientUtil;
import com.dms.api.common.utils.PageUtils;
import com.dms.api.common.utils.R;
import com.dms.api.common.utils.StringUtils;
import com.dms.api.core.config.HostsConfig;
import com.dms.api.core.constants.ConstantTable;
import com.dms.api.modules.controller.report.approvalquery.MyAuditController;
import com.dms.api.modules.entity.report.approvalquery.MyAuditEntity;
import com.dms.api.modules.entity.report.approvalquery.MyAuditPageRespEntity;
import com.dms.api.modules.service.report.approvalquery.MyAuditService;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author: hejiaxi
 * @CreateDate: 2018/9/6 17:07
 * @Description: 作用描述
 */

@Service
public class MyAuditServiceImpl implements MyAuditService {
    Logger logger = LoggerFactory.getLogger(MyAuditController.class);

    @Override
    public R queryList(Map<String, String> Params) {
        String url = HostsConfig.hostsAudit + ConstantTable.GET_MYVAC;

        Map<String, String> map = Maps.newHashMap();
        map.put("processDefineKey", Params.get("queryType"));
        map.put("userName", Params.get("userName"));
        map.put("page", Params.get("page"));
        map.put("limit", Params.get("limit"));

        logger.info("开始请求接口数据: url {} ,params {} ", url, map);
        String response = HttpClientUtil.doGet(url, map, 3000);

        JSONObject jsonObject = JSONObject.parseObject(response);
        String data = jsonObject.getString("data");
        String rc = jsonObject.getString("rc");
        String msg = jsonObject.getString("msg");
        logger.info("接口返回数据"+data);

        if (!StringUtils.equalsIgnoreCase(rc, "200")) {
            return R.error("接口请求异常" + msg);
        }

        MyAuditPageRespEntity pageResp = JSONObject.parseObject(data, MyAuditPageRespEntity.class);
        List<MyAuditEntity> dataList = pageResp.getList();
        PageUtils pageutils = new PageUtils(dataList, Integer.parseInt(pageResp.getTotal()), Integer.parseInt(Params.get("limit")), Integer.parseInt(Params.get("page")));

        return R.ok().put("page", pageutils);
    }


    @Override
    public R getProcdef() {
        String url = HostsConfig.hostsAudit + ConstantTable.GET_PROCDEF;

        logger.info("开始调用接口,获取审核组别列表数据,请求地址: " + url);
        String response = HttpClientUtil.doGet(url, 30000);

        logger.info("接口返回数据:" + response);
        JSONObject jsonObject = JSONObject.parseObject(response);
        String rc = jsonObject.getString("rc");
        String msg = jsonObject.getString("msg");

        if (!StringUtils.equalsIgnoreCase(rc, "200")) {
            return R.error("接口请求异常" + msg);
        }

        return R.ok().put("data", jsonObject.getJSONArray("data")).put("msg", "成功");
    }
}

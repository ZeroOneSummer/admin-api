package com.dms.api.modules.controller.sys.user;

import com.alibaba.fastjson.JSONObject;
import com.dms.api.core.config.HostsConfig;
import com.dms.api.core.constants.ConstantTable;
import com.dms.api.modules.base.AbstractController;
import com.dms.api.modules.service.sys.user.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 审批相关接口调用controller
 */
@RequestMapping("/audit")
@RestController
public class AuditController extends AbstractController {

    @Autowired
    private AuditService auditService;

    /**
     *
     * 获取用户审核组别列表
     * @return
     */
    @RequestMapping("/getGroup")
    public JSONObject getGroups() {
        logger.info("获取用户审核组别列表接口audit/getGroup入参：无");

        //响应格式
        JSONObject respJson = new JSONObject();
        respJson.put("msg", "FAILURE");
        respJson.put("rc", -1);
        respJson.put("data", null);

        //参数
        Map<String, String> reqParams = new HashMap <>();
        reqParams.put("groupType", "all");

        //调用接口
        String url = HostsConfig.hostsAudit + ConstantTable.GET_GROUP;
        logger.info("开始调用获取用户审核组别列表接口-请求 url:{}  param:{}", url , reqParams);
        JSONObject resp = auditService.getGroups(url, reqParams, respJson);
        logger.info("结束调用获取用户审核组别列表接口-响应结果 result:{}", resp);

        return resp;
    }

}




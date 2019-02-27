package com.dms.api.modules.controller.mall.order.info;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONObject;
import com.dms.api.common.utils.PageUtils;
import com.dms.api.common.utils.Query;
import com.dms.api.common.utils.R;
import com.dms.api.core.config.HostsConfig;
import com.dms.api.core.constants.ConstantTable;
import com.dms.api.modules.base.AbstractController;
import com.dms.api.modules.entity.mall.order.info.FullBillListEntity;
import com.dms.api.modules.entity.mall.order.info.FullBillPageRespEntity;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Author: hejiaxi
 * @CreateDate: 2018/9/10 11:36
 * @Description: 查询全款单列表
 */
@RestController
@RequestMapping("fullBill")
public class FullBillController extends AbstractController {
    Logger logger = LoggerFactory.getLogger(FullBillController.class);

    @Autowired
    HostsConfig hostsConfig;

    @RequestMapping("/list")
    @RequiresPermissions("fullBill:list")
    public R list(@RequestParam Map<String, Object> params) throws Exception {
        Query query = new Query(params);

        logger.info("fullBill-list入参:" + query);
        String url = hostsConfig.hostsI + ConstantTable.GET_ORDER;

        JSONObject jsonParams = new JSONObject();
        jsonParams.put("pageNo", query.getPage());
        jsonParams.put("pageSize", query.getLimit());
        jsonParams.put("subNumber", query.get("subNumber"));
        jsonParams.put("userId", query.get("userId"));
        jsonParams.put("orderType", query.get("orderType"));

        logger.info("开始请求接口数据  请求地址 + 请求参数:" + url + jsonParams);
        String result2 = HttpRequest.post(url).timeout(30000)
                .body(jsonParams.toJSONString())
                .execute().body();

        JSONObject jsonObject = JSONObject.parseObject(result2);
        String data = jsonObject.getString("data");

        logger.info("接口返回数据:" + data);
        int rc = jsonObject.getIntValue("rc");
        String msg = jsonObject.getString("msg");
        if (rc != 200) {
            return R.error("接口调用失败" + msg);
        }

        FullBillPageRespEntity pageResp = JSONObject.parseObject(data, FullBillPageRespEntity.class);

        List<FullBillListEntity> fullBillList = pageResp.getList();
        PageUtils page = new PageUtils(fullBillList, Integer.parseInt(pageResp.getCount()), Integer.parseInt(pageResp.getPageSize()), query.getPage());

        return R.ok().put("page", page);
    }
}

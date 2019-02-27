package com.dms.api.modules.controller.mall.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dms.api.common.utils.*;
import com.dms.api.core.config.HostsConfig;
import com.dms.api.core.constants.ConstantTable;
import com.dms.api.core.util.ConvertUtil;
import com.dms.api.modules.base.AbstractController;
import com.dms.api.modules.entity.mall.user.ModificationPhoneNumEntity;
import com.dms.api.modules.service.mall.user.ModificationPhoneNumService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("modificationPhoneNum")
public class ModificationPhoneNumController extends AbstractController {
    Logger logger = LoggerFactory.getLogger(ModificationPhoneNumController.class);

    @Resource
    ModificationPhoneNumService modificationPhoneNumService;

    @Resource
    ConvertUtil convertUtil;

    @Resource
    HostsConfig hostsConfig;


    @RequestMapping("/list")
    @RequiresPermissions("modificationPhoneNum:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询所有用户信息
        logger.info("开始查询用户信息，查询参数为：{}",params);
        Query query = new Query(params);
        query.put("sidx", StringUtils.camelToUnderline(query.get("sidx") + ""));

        List<ModificationPhoneNumEntity> list = modificationPhoneNumService.queryList(query);

        logger.info("查询的手机号码为:{}",params.get("phone"));
        int total = modificationPhoneNumService.queryTotal(query);

        logger.info("查询的数量为：{}",total);
        PageUtils pageUtil = new PageUtils(list, total, query.getLimit(), query.getPage());

        return R.ok().put("page",pageUtil);
    }

    @RequestMapping("/update")
    @RequiresPermissions("modificationPhoneNum:update")
    public JSONObject update(@RequestBody Map<String,Object> params) {
        logger.info(">>>调用修改手机号接口入参，{}",params);

        String url = hostsConfig.hostsI + ConstantTable.UPT_USER_PHONE;
        JSONObject jsonParams = new JSONObject();
        jsonParams.put("loginCode",params.get("loginCode"));
        jsonParams.put("newPhone",params.get("newPhone"));

        String sign = convertUtil.crypt(jsonParams);
        jsonParams.put("sign",sign);

        logger.info(">>>开始调用修改手机号接口: url {}, params {}",url , jsonParams);
        String response = HttpClientUtil.doPostJson(url, JSON.toJSONString(jsonParams),30000);
        logger.info("调用接口响应 {}",response);

        JSONObject jsonObject =JSONObject.parseObject(response);
        String rc = jsonObject.getString("rc");
        String msg = jsonObject.getString("msg");
        if (!StringUtils.equalsIgnoreCase(rc, "200")) {
            logger.info("调用修改手机号接口失败, 错误信息：{}", jsonObject);
            return jsonObject;
        }
        logger.info("调用修改手机号成功，返回结果：{}", jsonObject);

        return jsonObject;
    }
}

package com.dms.api.modules.service.sys.user.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dms.api.common.utils.HttpClientUtil;
import com.dms.api.common.utils.StringUtils;
import com.dms.api.modules.entity.sys.user.AuditUserGroup;
import com.dms.api.modules.service.sys.user.AuditService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AuditServiceImpl implements AuditService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    //获取用户审核组别列表
    @Override
    public JSONObject getGroups(String url, Map<String, String> reqParams, JSONObject respJson) {

        //开始调接口
        String response = HttpClientUtil.doGet(url, reqParams,30000);
        if (StringUtils.isBlank(response)){
            respJson.put("msg", "接口请求失败");
            logger.info("接口请求失败");
            return respJson;
        }

        //响应信息
        JSONObject jsonObject = JSONObject.parseObject(response);
        String rc = jsonObject.getString("rc");
        String msg = jsonObject.getString("msg");
        String data = jsonObject.getString("data");
        String exception = jsonObject.getString("exception");
        if (!StringUtils.isBlank(exception)) {
            logger.info("接口调用异常, 异常信息：{}", jsonObject.getString("message"));
        }
        if (!StringUtils.equalsIgnoreCase(rc, "200")) {
            logger.info("接口调用失败, 错误信息：{}", msg);
            respJson.put("msg", msg);
            return respJson;
        }

        respJson.put("rc", 200);
        respJson.put("msg", "SUCCESS");
        respJson.put("data", JSON.parseArray(data));
        logger.info("接口调用成功");

        return respJson;
    }

    //添加审核用户
    @Override
    public JSONObject addUsers(String url, Map<String, String> reqParams) {

        //响应格式
        JSONObject respJson = new JSONObject();
        respJson.put("rc", -1);
        respJson.put("msg", "FAILURE");
        respJson.put("data", null);

        //开始调接口
        logger.info("开始调用添加审核用户接口-请求 url:{}  param:{}", url , reqParams);

        String response = HttpClientUtil.doPostJson(url, JSON.toJSONString(reqParams),30000);
        if (StringUtils.isBlank(response)){
            respJson.put("msg", "接口请求异常");
            logger.info("接口请求异常");
            throw new RuntimeException("接口请求异常");
        }

        //响应信息
        JSONObject jsonObject = JSONObject.parseObject(response);
        JSONUtil.parseObj(response);
        String rc = jsonObject.getString("rc");
        String msg = jsonObject.getString("msg");
        String data = jsonObject.getString("data");
        String exception = jsonObject.getString("exception");
        if (!StringUtils.isBlank(exception)) {
            logger.info("接口调用异常, 异常信息：{}", jsonObject.getString("message"));
            throw new RuntimeException("接口请求异常");
        }
        if (!StringUtils.equalsIgnoreCase(rc, "200")) {
            logger.info("添加审核用户接口调用失败, 错误信息：{}", msg);
            respJson.put("msg", msg);
            throw new RuntimeException("接口调用失败");
        }

        respJson.put("rc", 200);
        respJson.put("msg", "SUCCESS");
        respJson.put("data", data);
        logger.info("添加审核用户接口调用成功，响应结果：{}", respJson);

        return respJson;
    }

    //更新审核用户组别
    @Override
    public JSONObject updateUserGroup(String url, Map<String, String> reqParams) {

        //响应格式
        JSONObject respJson = new JSONObject();
        respJson.put("rc", -1);
        respJson.put("msg", "FAILURE");
        respJson.put("data", null);

        //开始调接口
        logger.info("开始调用更新审核用户接口-请求 url:{}  param:{}", url , reqParams);

        String response = HttpClientUtil.doPostJson(url, JSON.toJSONString(reqParams),30000);
        if (StringUtils.isBlank(response)){
            respJson.put("msg", "接口请求异常");
            logger.info("接口请求失败");
            throw new RuntimeException("接口请求失败");
        }

        //响应信息
        JSONObject jsonObject = JSONObject.parseObject(response);
        JSONUtil.parseObj(response);
        String rc = jsonObject.getString("rc");
        String msg = jsonObject.getString("msg");
        String data = jsonObject.getString("data");
        String exception = jsonObject.getString("exception");
        if (!StringUtils.isBlank(exception)) {
            logger.info("接口调用异常, 异常信息：{}", jsonObject.getString("message"));
            throw new RuntimeException("接口请求异常");
        }
        if (!StringUtils.equalsIgnoreCase(rc, "200")) {
            logger.info("更新审核用户接口调用失败, 错误信息：{}", msg);
            respJson.put("msg", msg);
            throw new RuntimeException("接口请求失败");
        }

        respJson.put("rc", 200);
        respJson.put("msg", "SUCCESS");
        respJson.put("data", data);
        logger.info("更新审核用户接口调用成功，响应结果：{}", respJson);

        return respJson;
    }

    //查询审批用户组
    @Override
    public List<AuditUserGroup> queryAuditUserGroups(String url, Map<String, String> reqParams) {
        logger.info("查询审批用户组接口-请求：url: {} parmas: {}", url, reqParams);

        logger.info("开始调接口");
        //开始调接口
        String response = HttpClientUtil.doGet(url, reqParams,30000);
        if (StringUtils.isBlank(response)){
            logger.info("接口请求异常");
            return null;
        }

        //响应信息
        JSONObject jsonObject = JSONObject.parseObject(response);
        String rc = jsonObject.getString("rc");
        String msg = jsonObject.getString("msg");
        String data = jsonObject.getString("data");
        String exception = jsonObject.getString("exception");
        if (!StringUtils.isBlank(exception)) {
            logger.info("接口调用异常, 异常信息：{}", jsonObject.getString("message"));
        }
        if (!StringUtils.equalsIgnoreCase(rc, "200")) {
            logger.info("接口调用失败, 错误信息：{}", msg);
            return null;
        }

        //json数组转化为list
        JSONArray jsonArray = JSONUtil.parseArray(data);
        List<AuditUserGroup> AuditUserGroups = JSONUtil.toList(jsonArray, AuditUserGroup.class);
        logger.info("接口调用成功，返回结果：{}", AuditUserGroups);

        return AuditUserGroups;
    }
}

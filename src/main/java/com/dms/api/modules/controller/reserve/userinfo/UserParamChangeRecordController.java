package com.dms.api.modules.controller.reserve.userinfo;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dms.api.common.utils.PageUtils;
import com.dms.api.common.utils.R;
import com.dms.api.core.config.HostsConfig;
import com.dms.api.modules.entity.reserve.userinfo.UserParamChangeRecordEntity;
import com.dms.api.modules.service.reserve.userinfo.UserParamChangeRecordService;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @program: S-sys-admin-api
 * @Date: 2018/12/4 0004 17:09
 * @Author: hushuangxi
 * @Description:
 */
@RestController
@RequestMapping("/userParamChangeRecord")
public class UserParamChangeRecordController {
    private static Logger logger = LoggerFactory.getLogger(UserParamChangeRecordController.class);

    @Autowired
    private UserParamChangeRecordService userParamChangeRecordService;

    @RequestMapping("/list")
    //@RequiresPermissions("userParamChangeRecord:list")
    public R list(@RequestParam Map<String, Object> params) {
        logger.info("userParamChangeRecord/list查询参数：{}", params);

        String pageNo = params.get("page") == null ? null : (String) params.get("page");
        String pageSize = params.get("limit") == null ? null : (String) params.get("limit");

        //查询
        JSONObject jsonObject = this.queryRecordList(params, pageNo, pageSize, false);
        //结果非空判断
        if (jsonObject == null || jsonObject.getJSONObject("data") == null) return R.ok().put("page", null);

        JSONObject data = jsonObject.getJSONObject("data");
        String pho = data.getString("phone");
        String log = data.getString("logincode");
        String name = data.getString("username");

        JSONObject pageInfo = data.getJSONObject("pageInfo");
        int total = Integer.parseInt(pageInfo.getString("total"));

        List<UserParamChangeRecordEntity> list = null;
        JSONArray array = pageInfo.getJSONArray("list");
        if (array != null && array.size() > 0){
            String arrayStr = pageInfo.getString("list");
            list = JSONArray.parseArray(arrayStr , UserParamChangeRecordEntity.class);
            list.stream().forEach(entity -> {
                entity.setLogincode(log);
                entity.setUsername(name);
                entity.setPhone(pho);
            });
        }

        PageUtils pageUtil = new PageUtils(list, total, Integer.valueOf(pageSize), Integer.valueOf(pageNo));

        return R.ok().put("page", pageUtil);
    }

    @RequestMapping("/download")
    //@RequiresPermissions("userParamChangeRecord:download")
    public R download(@RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        logger.info("userParamChangeRecord/download导出参数：{}", params);

        String pageNo = params.get("page") == null ? null : (String) params.get("page");
        String pageSize = params.get("limit") == null ? null : (String) params.get("limit");

        //导出
        JSONObject jsonObject = this.queryRecordList(params, pageNo, pageSize, true);

        JSONObject data = jsonObject.getJSONObject("data");
        String pho = data.getString("phone");
        String log = data.getString("logincode");
        String name = data.getString("username");

        JSONObject pageInfo = data.getJSONObject("pageInfo");

        List<UserParamChangeRecordEntity> list = new ArrayList<>();
        JSONArray array = pageInfo.getJSONArray("list");
        if (array != null && array.size() > 0){
            String arrayStr = pageInfo.getString("list");
            list = JSONArray.parseArray(arrayStr , UserParamChangeRecordEntity.class);
            list.stream().forEach(entity -> {
                entity.setLogincode(log);
                entity.setUsername(name);
                entity.setPhone(pho);
            });
        }

        //报表参数对象
        ExportParams exportParams = new ExportParams();
        String[] exclu = {}; //忽略字段
        //报表名称
        String reportStr = "用户参数修改记录查询";
        exportParams.setExclusions(exclu);

        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, UserParamChangeRecordEntity.class, list);

        response.setHeader("content-Type", "application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 下载文件的默认名称
        String dataStr = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        response.setHeader("Content-Disposition", "attachment;filename="+new String((dataStr + reportStr).getBytes("utf-8"), "iso8859-1")+".xls");
        workbook.write(response.getOutputStream());

        return R.ok();
    }


    //查询接口数据
    private JSONObject queryRecordList(Map<String, Object> params, String pageNo, String pageSize, boolean flag){

        //查询列表数据
        String username = params.get("username") == null ? null : (String) params.get("username");
        String phone = params.get("phone") == null ? null : (String) params.get("phone");
        String logincode = params.get("logincode") == null ? null : (String) params.get("logincode");
        String startUpdateTime = params.get("startTime") == null ? null : (String) params.get("startTime");
        String endUpdateTime = params.get("endTime") == null ? null : (String) params.get("endTime");

        //参数封装
        Map<String, Object> reqParams = new HashMap<>();
        reqParams.put("username", username);
        reqParams.put("phone", phone);
        reqParams.put("loginCodeParam", logincode);
        reqParams.put("startUpdateTime", startUpdateTime);
        reqParams.put("endUpdateTime", endUpdateTime);
        reqParams.put("pageNo", pageNo);
        reqParams.put("pageSize", pageSize);
        reqParams.put("queryAll", flag); //true-导出，false-查询

        //请求地址&参数
        String ip = HostsConfig.hostsI;
        String url = ip + "/api/user/s/userRiskParams/find";
        JSONObject jsonObject = userParamChangeRecordService.getUserParamChangeRecordList(url, reqParams);

        return jsonObject;
    }

}

package com.dms.api.modules.controller.report.approvalquery;


import com.dms.api.common.utils.R;
import com.dms.api.common.utils.StringUtils;
import com.dms.api.modules.base.AbstractController;
import com.dms.api.modules.entity.sys.user.SysUserAudit;
import com.dms.api.modules.service.report.approvalquery.MyAuditService;
import com.dms.api.modules.service.sys.user.SysUserAuditService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author: hejiaxi
 * @CreateDate: 2018/9/6 16:56
 * @Description: 作用描述: 获取我申请中订单
 */
@RestController
@RequestMapping("myAudit")
public class MyAuditController extends AbstractController {
    Logger logger = LoggerFactory.getLogger(MyAuditController.class);

    @Resource
    SysUserAuditService sysUserAuditService;

    @Resource
    MyAuditService myAuditService;

    @RequestMapping("/list")
    @RequiresPermissions("myAudit:list")
    public R queryList(@RequestParam Map<String, String> params) {

        //获取审核用户名
        SysUserAudit sysUserAudit = sysUserAuditService.queryObject(getUser().getUserId());
        String auditorName = sysUserAudit == null ? null : sysUserAudit.getAuditorName();

        if (StringUtils.isBlank(auditorName)) {
            logger.info("当前登录用户没有对应的审核用户名");
            return null;
        }
        params.put("userName", auditorName);

        return myAuditService.queryList(params);
    }

    /**
     * 获取审批类型
     * @return
     */
    @RequestMapping("/getProcdef")
    public R getProcdef() {
        return myAuditService.getProcdef();
    }
}

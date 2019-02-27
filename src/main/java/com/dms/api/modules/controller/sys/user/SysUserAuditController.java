package com.dms.api.modules.controller.sys.user;

import com.dms.api.common.utils.PageUtils;
import com.dms.api.common.utils.Query;
import com.dms.api.common.utils.R;
import com.dms.api.core.config.HostsConfig;
import com.dms.api.core.constants.ConstantTable;
import com.dms.api.modules.base.AbstractController;
import com.dms.api.modules.entity.sys.user.AuditUserGroup;
import com.dms.api.modules.entity.sys.user.SysUserAudit;
import com.dms.api.modules.service.sys.user.AuditService;
import com.dms.api.modules.service.sys.user.SysUserAuditService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/sysUser")
@RestController
public class SysUserAuditController extends AbstractController {

    @Autowired
    SysUserAuditService sysUserAuditService;

    @Autowired
    AuditService auditService;

    @RequestMapping("/list")
    //@RequiresPermissions("sysUser:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        PageHelper.startPage(query.getPage(), query.getLimit());
        List<SysUserAudit> userList = sysUserAuditService.queryList(query);
        PageInfo<SysUserAudit> pageInfo = new PageInfo(userList);
        int total = (int)pageInfo.getTotal();

        //封装入审核信息
        List<AuditUserGroup> auditUserGroups = sysUserAuditService.queryAuditUserGroups(HostsConfig.hostsAudit + ConstantTable.GET_USERS_LIST);
        if (auditUserGroups != null && auditUserGroups.size() > 0){
            userList.stream().forEach(sysUser -> {
                auditUserGroups.stream().forEach(auditUser -> {
                    if (auditUser.getUserName().equals(sysUser.getAuditorName())) sysUser.setAuditUserGroup(auditUser);
                });
            });
        }

        PageUtils pageUtil = new PageUtils(userList, total, query);
        return R.ok().put("page", pageUtil);
    }

    //查询单条
    @RequestMapping("/info/{id}")
    public R getSysUserInfo(@PathVariable("id") Integer id) {

        SysUserAudit sysUserAudit = sysUserAuditService.queryObject(id);
        return R.ok().put("sysUserAudit", sysUserAudit);
    }

    //更新审批字段并调用添加用户接口
    @RequestMapping("/add")
    public R addSysUserInfo(@RequestBody SysUserAudit sysUserAudit) {
        //请求地址&参数
        String url = HostsConfig.hostsAudit + ConstantTable.ADD_USERS;
        Map<String, String> reqParams = new HashMap <>();
        reqParams.put("userName", sysUserAudit.getAuditorName()); //审核用户账号
        reqParams.put("groupId", sysUserAudit.getGroupId()); //所属审核组ID

        //接口和update在service层一起形成事务
        try {
            if (sysUserAuditService.add(url, reqParams, sysUserAudit) > 0) return R.ok();
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("添加审批用户信息失败，已回滚事务");
            return R.error("操作失败");
        }

        logger.info("添加审批用户信息失败");
        return R.error("操作失败");
    }

    //更新审批字段并调用更新用户组别接口
    @RequestMapping("/update")
    public R updateSysUserInfo(@RequestBody SysUserAudit sysUserAudit) {
        //请求地址&参数
        String url = HostsConfig.hostsAudit + ConstantTable.UPT_USERS_GROUP;
        Map<String, String> reqParams = new HashMap <>();
        reqParams.put("userName", sysUserAudit.getAuditorName()); //审核用户账号
        reqParams.put("newGroupId", sysUserAudit.getGroupId()); //所属审核组ID

        //接口和update在service层一起形成事务
        try {
            if (sysUserAuditService.update(url, reqParams, sysUserAudit) > 0) return R.ok();
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("更新审批用户信息失败，已回滚事务");
            return R.error("操作失败");
        }

        logger.info("更新审批用户信息失败");
        return R.error("操作失败");
    }

}

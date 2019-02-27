package com.dms.api.modules.service.sys.user.impl;

import com.alibaba.fastjson.JSONObject;
import com.dms.api.common.utils.StringUtils;
import com.dms.api.modules.dao.sys.user.SysUserAuditDao;
import com.dms.api.modules.entity.sys.user.AuditUserGroup;
import com.dms.api.modules.entity.sys.user.SysUserAudit;
import com.dms.api.modules.service.sys.user.AuditService;
import com.dms.api.modules.service.sys.user.SysUserAuditService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysUserAuditServiceImpl implements SysUserAuditService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    SysUserAuditDao sysUserAuditDao;

    @Autowired
    AuditService auditService;

    @Override
    public List <SysUserAudit> queryList(Map <String, Object> map) {
        return sysUserAuditDao.queryList(map);
    }

    @Override
    public List <AuditUserGroup> queryAuditUserGroups(String url) {
        Map<String, String> reqParams = new HashMap <>();
        reqParams.put("groupType", "all");
        return auditService.queryAuditUserGroups(url, reqParams);
    }

    @Override
    public SysUserAudit queryObject(Object id) {
        return sysUserAuditDao.queryObject(id);
    }

    @Transactional
    @Override
    public int add(String url, Map<String, String> reqParams, SysUserAudit sysUserAudit) {

        if(sysUserAuditDao.update(sysUserAudit) > 0){  //更新数据库
            JSONObject jsonObject =  auditService.addUsers(url, reqParams); //调接口
            if (!StringUtils.equalsIgnoreCase(jsonObject.getString("rc"), "200")){ //成功
                throw new RuntimeException("接口添加用户数据失败");
            }else{
                return 1; //成功
            }
        }
        return -1; //失败
    }

    @Transactional
    @Override
    public int update(String url, Map<String, String> reqParams, SysUserAudit sysUserAudit) {

        if(sysUserAuditDao.update(sysUserAudit) > 0){  //更新数据库
            JSONObject jsonObject =  auditService.updateUserGroup(url, reqParams); //调接口
            if (!StringUtils.equalsIgnoreCase(jsonObject.getString("rc"), "200")){ //成功
                throw new RuntimeException("接口更新用户组别失败");
            }else{
                return 1; //成功
            }
        }
        return -1; //失败
    }

    @Override
    public String queryPayId(String loginCode) {
        return sysUserAuditDao.queryPayId(loginCode);
    }

}

package com.dms.api.modules.dao.sys.user;


import com.dms.api.modules.base.BaseDao;
import com.dms.api.modules.entity.sys.user.SysUserAudit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface SysUserAuditDao extends BaseDao<SysUserAudit> {

    String queryPayId(@Param("loginCode") String loginCode); //查询签约席位号

    List<Map<String, Object>> queryMallUserInfo(@Param("loginCode") String loginCode); //查询商城用户信息

}
package com.dms.api.modules.entity.sys.user;

import lombok.Data;

import java.util.Date;

/**
 * @author Administrator
 * @Description: 系统用户审批组
 * @date 2018/9/4 15:22
 */
@Data
public class SysUserAudit {

    private Long userId; //用户ID
    private String userName; //用户名
    private Integer dealerId; //服务商ID
    private Integer orgId; //机构ID
    private String password; //密码
    private String salt; //盐
    private String email; //邮箱
    private String mobile; //手机号
    private Integer status; //状态  0-禁用 1-正常
    private Integer createUserId; //创建者ID
    private Date createTime; //创建时间
    private Integer userType; //用户类型，默认为1；1：普通用户，0：子超管，-1：超管
    private String auditorName; //审批用户名
    private String groupId; //用户所属审核组ID
    private AuditUserGroup auditUserGroup; //用户审核组
}

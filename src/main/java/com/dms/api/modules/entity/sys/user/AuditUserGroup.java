package com.dms.api.modules.entity.sys.user;

import lombok.Data;

/**
 * @author Administrator
 * @Description: 审批用户组
 * @date 2018/9/4 15:22
 */
@Data
public class AuditUserGroup {

    private String userName; //用户账号
    private String password; //密码
    private String groupId; //组id
    private String groupName; //组名称
    private String type; //状态 true-启用

}

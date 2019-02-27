package com.dms.api.modules.entity.sys.user;

import com.dms.api.common.utils.Constant;
import com.dms.api.common.validator.group.UpdateGroup;
import com.dms.api.common.validator.group.AddGroup;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 系统用户
 */
@Data
public class SysUserEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 用户ID
	 */
	private Long userId;
	/**
	 * 用户名
	 */
	@NotBlank(message="用户名不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String username;
	/**
	 * 密码
	 */
	@NotBlank(message="密码不能为空", groups = AddGroup.class)
	private String password;
	/**
	 * 盐
	 */
	private String salt;

	/**
	 * 邮箱
	 */
	@Email(message="邮箱格式不正确", groups = {AddGroup.class, UpdateGroup.class})
	private String email;
	/**
	 * 手机号
	 */
	private String mobile;
	/**
	 * 状态  0：禁用   1：正常
	 */
	private Integer status;
	/**
	 * 角色ID列表
	 */
	private List<Long> roleIdList;
	/**
	 * 创建者ID
	 */
	private Long createUserId;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 用户类型
	 */
	private Integer userType;
	/**
	 * 机构Id
	 */
	private Long orgId;
	/**
	 * 商家Id
	 */
	private Long dealerId;
	private boolean adminEd;
	private String traPwd; //交易密码
	private String newTraPwd; //新交易密码
	private String smsCode; //短信验证码
	private String auditorName;//审批用户名
	private String groupId;//所属审批组ID


	/**
	 * 判断是否为系统超管或子超管
	 * @return boolean
	 */
	public boolean isSuperAdmin() {
		return this.userType == Constant.SYS_SUPER_ADMIN || this.userType == Constant.SUPER_ADMIN;
	}

	/**
	 * 判断是否为系统超管
	 * @return boolean
	 */
	public boolean isSysSuperAdmin() {
		return this.userType == Constant.SYS_SUPER_ADMIN;
	}
}

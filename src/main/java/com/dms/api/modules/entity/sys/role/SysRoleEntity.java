package com.dms.api.modules.entity.sys.role;

import com.dms.api.common.utils.Constant;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 角色
 */
@Data
public class SysRoleEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 角色ID
	 */
	private Long roleId;
	/**
	 * 角色名称
	 */
	@NotBlank(message="角色名称不能为空")
	private String roleName;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 创建者ID
	 */
	private Long createUserId;
	private List<Long> menuIdList;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 角色类型
	 */
	private Integer roleType;
	/**
	 * 判断是否为子超管角色
	 * @return
	 */
	public boolean isSuperAdminRole(){
		return this.roleType == Constant.SUPER_ADMIN;
	}
}

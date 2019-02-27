package com.dms.api.modules.entity.sys.role;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 角色与菜单对应关系
 */
@Data
public class SysRoleMenuEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	/**
	 * 角色ID
	 */
	private Long roleId;
	/**
	 * 菜单ID
	 */
	private Long menuId;
}

package com.dms.api.modules.entity.sys.user;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class SysUserOrgEntity {
	private Long id;

	/**
	 * userID
	 */
	private Long userId;
	/**
	 * 机构ID
	 */
	private Long orgId;
}

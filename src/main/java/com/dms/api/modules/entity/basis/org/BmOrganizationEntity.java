package com.dms.api.modules.entity.basis.org;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 机构表
 * 
 */
@Data
public class BmOrganizationEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;			//机构表ID,自增ID
	private Long orgId;			//机构ID
	private String bmOrgName;	//机构名称
	private String bmOrgNum;	//机构编号
	private String bmOrgType;	//机构类型
	private Long bmOrgParent;	//机构上级
	private Date intoDate;		//新建时间
	private String intoUser;	//新建用户
	private Date updDate;		//更新时间
	private String updUser;		//更新用户
	private String remarks;		//备注

}

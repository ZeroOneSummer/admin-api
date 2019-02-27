package com.dms.api.modules.entity.reserve.level;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 等级申请表
 * 
 * @author
 * @email
 * @date
 */
@Data
public class SysLevelapplyEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//唯一标识ID，自增
	private Integer id;
	//用户唯一标识
	private String logincode;
	//申请前等级编号
	private String prelevelcode;
	//申请等级编号
	private String targetlevelcode;
	//审核状态。默认为申请中:APPLY_PASS,APPLY_PASS
	private String status;
	//申请时间
	private Date createtime;
	//审核时间
	private Date verifytime;
	//修改时间
	private Date updatetime;
	//新建用户（操作者）
	private Long operator;
	//审核备注
	private String remark;

}

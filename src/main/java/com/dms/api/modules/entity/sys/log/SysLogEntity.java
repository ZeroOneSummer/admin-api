package com.dms.api.modules.entity.sys.log;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统日志
 * 
 */
@Data
public class SysLogEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	//用户名
	private String username;
	//用户操作
	private String operation;
	//请求方法
	private String method;
	//请求参数
	private String params;
	//执行时长(毫秒)
	private Long time;
	//IP地址
	private String ip;
	//创建时间
	private Date createDate;
}

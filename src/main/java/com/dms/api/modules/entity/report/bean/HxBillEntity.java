package com.dms.api.modules.entity.report.bean;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 账单管理表
 * @author
 * @email @danpacmall.com
 * @date 2017-11-07 14:39:18
 */
@Data
public class HxBillEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键id
	private Long id;
	//用户创建时间
	private Date creattime;
	//更新时间
	private Date updatetime;
	//本期开始时间
	private Date currentbegintime;
	//本期结束时间
	private Date currentendtime;
	//日期区间标识
	private String intervalidentification;
	//上期期末结余
	private BigDecimal previousbalance;
	//本期结余
	private BigDecimal currentbalance;
	//期数
	private Integer periodsnumber;
	//用户id
	private String userid;
	//账单是否有效
	private Integer isvalid;
}

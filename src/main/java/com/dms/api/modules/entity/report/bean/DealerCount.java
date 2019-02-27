package com.dms.api.modules.entity.report.bean;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class DealerCount {
	
	private long dealerId;

	private long accId;
	
	//服务商
	private String baseInfoName;
	
	//K豆总额
	@Excel(name = "收入K豆总额", orderNum = "3", mergeVertical = false, isImportField = "total",width=20)
	private BigDecimal total;
	
	//可用K豆
	@Excel(name = "当前可用K豆余额", orderNum = "6", mergeVertical = false, isImportField = "usable",width=20)
	private BigDecimal usable;

	//已消费K豆
	@Excel(name = "支出K豆总额", orderNum = "4", mergeVertical = false, isImportField = "consume",width=20)
	private BigDecimal consume;
	
	//失效K豆
	@Excel(name = "失效K豆总额", orderNum = "5", mergeVertical = false, isImportField = "invalid",width=20)
	private BigDecimal invalid;
	
	//次日过期K豆
	@Excel(name = "15日内即将过期K豆", orderNum = "7", mergeVertical = false, isImportField = "morrow",width=20)
	private BigDecimal morrow;
	
	//服务商名称
	@Excel(name = "服务商名称", orderNum = "2", mergeVertical = false, isImportField = "dealerName",width=30)
	private String dealerName;

	//服务商编号
	@Excel(name = "服务商编码", orderNum = "1", mergeVertical = false, isImportField = "dealerCode",width=20)
	private String dealerCode;

	public void init(){
		BigDecimal inital = new BigDecimal(0);
		this.setInvalid(inital);
		this.setConsume(inital);
		this.setTotal(inital);
		this.setMorrow(inital);
		this.setUsable(inital);
	}
}

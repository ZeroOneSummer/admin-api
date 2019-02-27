package com.dms.api.modules.entity.report.bean;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SingleCount {
	
	private String userId;
	
	private long accId;

	//客户姓名
//	@Excel(name = "客户姓名", orderNum = "1", mergeVertical = false, isImportField = "nickName",width=20)
	private String nickName;

	//客户登录账户
	@Excel(name = "客户登录账号", orderNum = "1", mergeVertical = false, isImportField = "loginCode",width=40)
	private String loginCode;
	
	//手机号码
	@Excel(name = "手机号码", orderNum = "2", mergeVertical = false, isImportField = "userMobile",width=20)
	private String userMobile;
	
	//所属订货分部，服务商名称
//	@Excel(name = "订货分部", orderNum = "3", mergeVertical = false, isImportField = "baseInfoName",width=20)
	private String baseInfoName;
	
	//服务商名称
//	@Excel(name = "所属服务商", orderNum = "4", mergeVertical = false, isImportField = "dealerName",width=20)
	private String dealerName;
	
	//服务商编号
	@Excel(name = "所属服务商", orderNum = "4", mergeVertical = false, isImportField = "dealerCode",width=20)
	private String dealerCode;
	
	//机构名
//	@Excel(name = "所属机构", orderNum = "5", mergeVertical = false, isImportField = "orgName",width=20)
	private String orgName;

	//机构名
	@Excel(name = "所属机构", orderNum = "5", mergeVertical = false, isImportField = "orgCode",width=20)
	private String orgCode;
	
	//K豆总额
	@Excel(name = "收入K豆总额", orderNum = "6", mergeVertical = false, isImportField = "total",width=20)
	private BigDecimal total;
	
	//可用K豆
	@Excel(name = "当前可用K豆总额", orderNum = "9", mergeVertical = false, isImportField = "usable",width=20)
	private BigDecimal usable;
	
	//已消费的K豆
	@Excel(name = "支出K豆总额", orderNum = "7", mergeVertical = false, isImportField = "consume",width=20)
	private BigDecimal consume;
	
	//失效K豆，过期K豆
	@Excel(name = "失效K豆总额", orderNum = "8", mergeVertical = false, isImportField = "invalid",width=20)
	private BigDecimal invalid;
	
	//账单日期
	@Excel(name = "账单日", orderNum = "10", mergeVertical = false,databaseFormat = "yyyyMMddHHmmss", format = "yyyy-MM-dd HH:mm:ss", isImportField = "currentEndTime",width=30)
	private Integer currentEndTime;
	
	//即将失效K豆
	@Excel(name = "15日内即将过期K豆", orderNum = "11", mergeVertical = false, isImportField = "willExpire",width=20)
	private BigDecimal willExpire;

	public void init(){
		BigDecimal inital = new BigDecimal(0);
		this.setInvalid(inital);
		this.setConsume(inital);
		this.setTotal(inital);
		this.setWillExpire(inital);
		this.setUsable(inital);
	}
}

package com.dms.api.modules.entity.report.bean;

import java.math.BigDecimal;
import java.sql.Date;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

@Data
public class SingleConsumeDetail {

	private String userId;

	//客户名称
	@Excel(name = "客户姓名", orderNum = "1", mergeVertical = false, isImportField = "nickName",width=20)
	private String nickName;

	//K豆类型(0：获取,1.消费)
	@Excel(name = "K豆类型", orderNum = "2",replace = { "获取_0", "消费_1","消费_2" }, mergeVertical = false, isImportField = "type",width=20)
	private String type;

	//K豆类型(0：获取,1.消费)
//	@Excel(name = "K豆类型", orderNum = "2",replace = { "获取_0", "消费_1","消费_2" }, mergeVertical = false, isImportField = "activeType",width=20)
	private Integer activeType;

	//获取类型名称
	private String orderTypeName;

	//K豆消费数量
	@Excel(name = "使用数量", orderNum = "3", mergeVertical = false, isImportField = "total",width=20)
	private BigDecimal total;

	//消费时间
	@Excel(name = "时间", orderNum = "4", mergeVertical = false, isImportField = "date",width=20)
	private Date date;

	//订单号
	@Excel(name = "订单号", orderNum = "5", mergeVertical = false, isImportField = "orderNo",width=30)
	private String orderNo;

	//订单类型
	@Excel(name = "订单类型", orderNum = "6", mergeVertical = false, isImportField = "orderType",width=20)
	private String orderType;

	//商品信息
	@Excel(name = "产品类型", orderNum = "7", mergeVertical = false, isImportField = "skuInfo",width=40)
	private String skuInfo;

	//订单金额
	@Excel(name = "订单金额", orderNum = "8", mergeVertical = false, isImportField = "orderMoney",width=20)
	private BigDecimal orderMoney;

	//实付金额
	@Excel(name = "实付金额", orderNum = "9", mergeVertical = false, isImportField = "payment",width=20)
	private BigDecimal payment;

	//全款单K豆退差价
	@Excel(name = "全款单K豆退差价", orderNum = "10", mergeVertical = false, isImportField = "refundDiff",width=20)
	private BigDecimal refundDiff;

}

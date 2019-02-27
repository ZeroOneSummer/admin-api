package com.dms.api.modules.entity.report.bean;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 服务商/机构K豆日结表
 * @author 40
 * @email chenshiling@danpacmall.com
 * @date 2018-01-16 10:55:44
 */
@Data
public class BountyDailySettlementEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键ID
	private Long id;
	//服务商id
	private String dealerId;
	//机构id
	private String orgId;
	//预订日
	@Excel(name = "预订日", orderNum = "1", mergeVertical = false, isImportField = "tradingDay",width=20)
	private String tradingDay;
	//总额
	private BigDecimal total;
	//收入
	@Excel(name = "收入总额", orderNum = "2", mergeVertical = false, isImportField = "income",width=20)
	private BigDecimal income;
	//支出
	@Excel(name = "支出总额", orderNum = "3", mergeVertical = false, isImportField = "income",width=20)
	private BigDecimal consume;
	//失效
	@Excel(name = "失效总额", orderNum = "4", mergeVertical = false, isImportField = "income",width=20)
	private BigDecimal invalid;

	public BountyDailySettlementEntity() {}

	public BountyDailySettlementEntity( String dealerId, String orgId, String tradingDay, BigDecimal total, BigDecimal income, BigDecimal consume, BigDecimal invalid) {
		this.dealerId = dealerId;
		this.orgId = orgId;
		this.tradingDay = tradingDay;
		this.total = total;
		this.income = income;
		this.consume = consume;
		this.invalid = invalid;
	}
}

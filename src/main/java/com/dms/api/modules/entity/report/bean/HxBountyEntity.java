package com.dms.api.modules.entity.report.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * 奖励金_资金流水表
 * @author
 * @email @danpacmall.com
 * @date 2017-11-07 09:57:36
 */
@Data
public class HxBountyEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	@Excel(name = "编号", orderNum = "1", mergeVertical = true, isImportField = "id")
	private Long id;
	//奖励金
	@Excel(name = "奖励金", orderNum = "2", mergeVertical = true, isImportField = "price")
	private BigDecimal price;
	//0 用户下单  1 奖励金划转到现货部
	private Integer type;
	//创建时间
	private Date createTime;
	//sku
	private String sku;
	//订单编号
	private String orderNo;
	//付款人id
	private String draweeId;
	//收款人id
	private String payeeId;
	//备注
	private String remark;
	//状态  0 下单成功  1 奖励金划款成功
	private Integer status;
	//修改时间
	private Date updateTime;
	//操作的用户id
	private String userId;
	//0 未失效  1 已失效
	private Integer isActive;
	//账户结余
	private BigDecimal cashSurplus;
	//所属商家id
	private String dealerId;
}

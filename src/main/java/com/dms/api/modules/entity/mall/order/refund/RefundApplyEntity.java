package com.dms.api.modules.entity.mall.order.refund;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 退差价申请表
 * 
 * @author
 * @email @danpacmall.com
 * @date 2017-12-08 16:25:13
 */
@Data
public class RefundApplyEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	//主键ID
	private Long id;
	//全额订单单号
	private String orderCode;
	//用户姓名
	private String userName;
	//用户资金账号id
	private String accId;
	//订单金额
	private BigDecimal orderPrice;
	//退差价金额
	private BigDecimal refundMoney;
	//退差K豆数量
	private BigDecimal refundBean;
	//退差价总额
	private BigDecimal refundTotal;
	//申请时间
	private Date applyTime;
	//备注信息
	private String remarks;
}

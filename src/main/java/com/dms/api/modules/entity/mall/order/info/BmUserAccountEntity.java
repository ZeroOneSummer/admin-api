package com.dms.api.modules.entity.mall.order.info;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 资金账户表-----B系统表名为：user_Account
 */
@Data
public class BmUserAccountEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键ID
	private Long id;
	//资金帐号id
	private String accId;
	//用户id
	private String userId;
	//资金帐号状态
	private String accStatus;
	//资金帐号类型
	private String accType;
	//市场id
	private String tradeMarketId;
	//商品属性id
	private String propertyTypeId;
	//所属id
	private String dealerId;
	//备注
	private String remarks;

	//对应商家信息
	private String dealerName;
	private String dealerCode;
	private String dealerType;
	//对应用户信息
	private String userName;

}

package com.dms.api.modules.entity.mall.order.info;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 商品信息-----B系统表名为：symbol_Info
 */
@Data
public class BmSymbolInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键ID
	private Long id;
	//商品Id primaryKeyName
	private String symbolId;
	//产品类型
	private String symbolName;
	//商品代码
	private String symbolCode;
	//备注
	private String remarks;

}

package com.dms.api.modules.entity.reserve.level;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 等级周期限制信息表
 *
 * @author jp
 * @email @danpacmall.com
 * @date 2018-06-21 15:11:02
 */
@Data
public class SysLevelinfoCycleEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	//唯一标识
	private Integer id;
	//等级编号，唯一
	private String levelCode;
	//周期类型 D日 W周 M月
	private String cycleType;
	/**最大订单数**/
	private Integer maxOrderNum;
	/**最大克重 ( 金 )**/
	private BigDecimal maxWeightAu;
	/**最大克重 ( 银 )**/
	private BigDecimal maxWeightAg;
	/**最大预付款**/
	private BigDecimal maxAdvance;
	/**最大净预付款**/
	private BigDecimal maxNetAdvance;
	//数据增加时间
	private Date createTime;
	//数据修改时间
	private Date updateTime;
	//备注
	private String remarks;
	//是否启用该限制条件(启用:FLAG_YES,不启用:FLAG_NO)
	private String status;

}

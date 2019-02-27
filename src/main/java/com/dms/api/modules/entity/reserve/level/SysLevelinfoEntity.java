package com.dms.api.modules.entity.reserve.level;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 等级信息表
 * 
 * @author
 * @email @danpacmall.com
 * @date 2017-08-28 13:46:02
 */
@Data
public class SysLevelinfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//唯一标识
	private Integer id;
	//等级编号，唯一
	private String levelcode;
	//等级名称
	private String levelname;
	//该等级是否需要人工审核   NO_NEED_VERIFY:该等级申请不需要审核,NEED_VERIFY:该等级申请需要审核
	private String applyed;
	//是否启用该等级,启用状态:FLAG_YES,不启用状态:FLAG_NO
	private String flag;
	//等级资产要求
	private BigDecimal moneyrequire;
	//同时预订金额最大
	private BigDecimal holdorderrequire;
	//最大产品规格 ( 金 )
	private BigDecimal maxorderau;
	//最大产品规格 ( 银 )
	private BigDecimal maxorderag;
	//单次预订最多 ( 金 )
	private BigDecimal maxoneau;
	//单次预订最多 ( 银 )
	private BigDecimal maxoneag;
	//数据增加时间
	private Date createtime;
	//数据修改时间
	private Date updatetime;
	//备注
	private String remarks;
	//是否默认等级
	private String isDefault;
}

package com.dms.api.modules.entity.mall.user;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商城用户等级表
 * 
 * @author
 * @email @danpacmall.com
 * @date 2017-08-25 18:09:36
 */
public class DfMallUserLevelInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//表自增id
	private Long id;
	//等级Id
	private String ulId;
	//等级名称
	private String ulLevelName;
	//等级资产要求
	private BigDecimal ulMoneyRequire;
	//同时预订金额最大
	private BigDecimal ulHoldOrderRequire;
	//最大产品规格 ( 金 )
	private BigDecimal ulMaxOrderAu;
	//最大产品规格 ( 银 )
	private BigDecimal ulMaxOrderAg;
	//单次预订最多 ( 金 )
	private BigDecimal ulMaxOneAu;
	//单次预订最多 ( 银 )
	private BigDecimal ulMaxOneAg;
	//数据插入时间
	private Date gmtCreate;
	//数据修改时间
	private Date gmtModified;
	//备注
	private String remarks;

	/**
	 * 设置：表自增id
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：表自增id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：等级Id
	 */
	public void setUlId(String ulId) {
		this.ulId = ulId;
	}
	/**
	 * 获取：等级Id
	 */
	public String getUlId() {
		return ulId;
	}
	/**
	 * 设置：等级名称
	 */
	public void setUlLevelName(String ulLevelName) {
		this.ulLevelName = ulLevelName;
	}
	/**
	 * 获取：等级名称
	 */
	public String getUlLevelName() {
		return ulLevelName;
	}
	/**
	 * 设置：等级资产要求
	 */
	public void setUlMoneyRequire(BigDecimal ulMoneyRequire) {
		this.ulMoneyRequire = ulMoneyRequire;
	}
	/**
	 * 获取：等级资产要求
	 */
	public BigDecimal getUlMoneyRequire() {
		return ulMoneyRequire;
	}
	/**
	 * 设置：同时预订金额最大
	 */
	public void setUlHoldOrderRequire(BigDecimal ulHoldOrderRequire) {
		this.ulHoldOrderRequire = ulHoldOrderRequire;
	}
	/**
	 * 获取：同时预订金额最大
	 */
	public BigDecimal getUlHoldOrderRequire() {
		return ulHoldOrderRequire;
	}
	/**
	 * 设置：最大产品规格 ( 金 )
	 */
	public void setUlMaxOrderAu(BigDecimal ulMaxOrderAu) {
		this.ulMaxOrderAu = ulMaxOrderAu;
	}
	/**
	 * 获取：最大产品规格 ( 金 )
	 */
	public BigDecimal getUlMaxOrderAu() {
		return ulMaxOrderAu;
	}
	/**
	 * 设置：最大产品规格 ( 银 )
	 */
	public void setUlMaxOrderAg(BigDecimal ulMaxOrderAg) {
		this.ulMaxOrderAg = ulMaxOrderAg;
	}
	/**
	 * 获取：最大产品规格 ( 银 )
	 */
	public BigDecimal getUlMaxOrderAg() {
		return ulMaxOrderAg;
	}
	/**
	 * 设置：单次预订最多 ( 金 )
	 */
	public void setUlMaxOneAu(BigDecimal ulMaxOneAu) {
		this.ulMaxOneAu = ulMaxOneAu;
	}
	/**
	 * 获取：单次预订最多 ( 金 )
	 */
	public BigDecimal getUlMaxOneAu() {
		return ulMaxOneAu;
	}
	/**
	 * 设置：单次预订最多 ( 银 )
	 */
	public void setUlMaxOneAg(BigDecimal ulMaxOneAg) {
		this.ulMaxOneAg = ulMaxOneAg;
	}
	/**
	 * 获取：单次预订最多 ( 银 )
	 */
	public BigDecimal getUlMaxOneAg() {
		return ulMaxOneAg;
	}
	/**
	 * 设置：数据插入时间
	 */
	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}
	/**
	 * 获取：数据插入时间
	 */
	public Date getGmtCreate() {
		return gmtCreate;
	}
	/**
	 * 设置：数据修改时间
	 */
	public void setGmtModified(Date gmtModified) {
		this.gmtModified = gmtModified;
	}
	/**
	 * 获取：数据修改时间
	 */
	public Date getGmtModified() {
		return gmtModified;
	}
	/**
	 * 设置：备注
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	/**
	 * 获取：备注
	 */
	public String getRemarks() {
		return remarks;
	}
}

package com.dms.api.modules.entity.mall.user;

import java.io.Serializable;
import java.util.Date;

/**
 * 商城用户表
 * 
 * @author
 * @email @danpacmall.com
 * @date 2017-08-25 18:09:35
 */
public class DfMallUserInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//表自增id
	private Long id;
	//user_InfoBrief 的 logincode
	private String uUserLogincode;
	//用户级别id
	private String uLevelId;
	//是否有推荐码 ？ 0 : 没；1：有
	private Integer uIsSerialcode;
	//是否同意签订风险揭示书 ？ 0：否；1：是
	private Integer uIsRisk;
	//是否实名认证：0 未实名认证  1 实名认证成功
	private Integer uIsAuth;
	//数据插入时间
	private Date gmtCreate;
	//数据修改时间
	private Date gmtModified;
	//备注
	private String remarks;
	//0:未审核,1:审核通过,2:审核不通过
	private Integer checkStatus;

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
	 * 设置：user_InfoBrief 的 logincode
	 */
	public void setUUserLogincode(String uUserLogincode) {
		this.uUserLogincode = uUserLogincode;
	}
	/**
	 * 获取：user_InfoBrief 的 logincode
	 */
	public String getUUserLogincode() {
		return uUserLogincode;
	}
	/**
	 * 设置：用户级别id
	 */
	public void setULevelId(String uLevelId) {
		this.uLevelId = uLevelId;
	}
	/**
	 * 获取：用户级别id
	 */
	public String getULevelId() {
		return uLevelId;
	}
	/**
	 * 设置：是否有推荐码 ？ 0 : 没；1：有
	 */
	public void setUIsSerialcode(Integer uIsSerialcode) {
		this.uIsSerialcode = uIsSerialcode;
	}
	/**
	 * 获取：是否有推荐码 ？ 0 : 没；1：有
	 */
	public Integer getUIsSerialcode() {
		return uIsSerialcode;
	}
	/**
	 * 设置：是否同意签订风险揭示书 ？ 0：否；1：是
	 */
	public void setUIsRisk(Integer uIsRisk) {
		this.uIsRisk = uIsRisk;
	}
	/**
	 * 获取：是否同意签订风险揭示书 ？ 0：否；1：是
	 */
	public Integer getUIsRisk() {
		return uIsRisk;
	}
	/**
	 * 设置：是否实名认证：0 未实名认证  1 实名认证成功
	 */
	public void setUIsAuth(Integer uIsAuth) {
		this.uIsAuth = uIsAuth;
	}
	/**
	 * 获取：是否实名认证：0 未实名认证  1 实名认证成功
	 */
	public Integer getUIsAuth() {
		return uIsAuth;
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
	/**
	 * 设置：0:未审核,1:审核通过,2:审核不通过
	 */
	public void setCheckStatus(Integer checkStatus) {
		this.checkStatus = checkStatus;
	}
	/**
	 * 获取：0:未审核,1:审核通过,2:审核不通过
	 */
	public Integer getCheckStatus() {
		return checkStatus;
	}
}

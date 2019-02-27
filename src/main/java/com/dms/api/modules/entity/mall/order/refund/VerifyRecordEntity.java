package com.dms.api.modules.entity.mall.order.refund;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 审核表
 * 
 * @author
 * @email @danpacmall.com
 * @date 2017-12-08 16:25:13
 */
@Data
public class VerifyRecordEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键ID
	private Long id;
	//审核类型；0：初审，1：复审，2：三审...
	private Integer type;
	//审核状态；-1：驳回，0：审核中，1：通过
	private Integer status;
	//是否需要下一步审核;0：否，1：是
	private boolean nextVerify;
	//审核时间
	private Date verifyTime;
	//关联类型；REFUND_APPLY：退差价审核,...
	private String referType;
	//关联id
	private String referId;
	//审核人id
	private Long verifyBy;
	//备注
	private String remark;

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public boolean isNextVerify() {
		return nextVerify;
	}

	public void setNextVerify(boolean nextVerify) {
		this.nextVerify = nextVerify;
	}

	public VerifyRecordEntity() {
	}

	public VerifyRecordEntity(Long id, Integer type, Integer status, Date verifyTime, String referType, String referId, Long verifyBy) {
		this.id = id;
		this.type = type;
		this.status = status;
		this.verifyTime = verifyTime;
		this.referType = referType;
		this.referId = referId;
		this.verifyBy = verifyBy;
	}
}

package com.dms.api.modules.entity.reserve.shiftorg;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户转移机构记录表
 * 
 * @author 40
 * @email chenshiling@danpacmall.com
 * @date 2018-01-09 13:53:46
 */
@Data
public class ShiftOrgRecordEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	//转移机构记录ID
	private Long id;

	private Long vId;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Long getvId() {
		return vId;
	}

	public void setvId(Long vId) {
		this.vId = vId;
	}
	//转移状态；-1：失败，0：待转移，1：成功
	private Integer status;
	//处理结果
	@Excel(name = "处理结果", orderNum = "10", width = 30)
	private String statusSt;

	//用户登录账号
	@Excel(name = "登录账号", orderNum = "3", width = 50)
	private String loginCode;

	//转移机构推荐码
//	@Excel(name = "目标机构推荐码", orderNum = "7", width = 20)
	private String serialCode;

	//创建时间
	@Excel(name = "提交时间", orderNum = "8", width = 20, format = "YYYY-MM-dd HH:mm:ss")
	private Date createTime;

	//源机构编号
	@Excel(name = "源机构编号", orderNum = "5", width = 20)
	private String srcOrgCode;
	//目标机构编号
	@Excel(name = "目标机构编号", orderNum = "6", isImportField = "tarOrgCode", width = 20)
	private String tarOrgCode;
	//客户名称
	@Excel(name = "客户名称", orderNum = "2", isImportField = "userName", width = 20)
	private String userName;
	//客户手机号码
//	@Excel(name = "客户手机号码", orderNum = "4", isImportField = "phoneNumber", width = 20)
	private String phoneNumber;
	//备注
	@Excel(name = "备注", orderNum = "11", width = 80)
	private String remarks;
	//转移时间
	@Excel(name = "处理时间", orderNum = "9", width = 20, format = "YYYY-MM-dd HH:mm:ss")
	private Date shiftTime;

}

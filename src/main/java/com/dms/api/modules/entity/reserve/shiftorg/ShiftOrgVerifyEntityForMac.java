package com.dms.api.modules.entity.reserve.shiftorg;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户转移机构记录表
 * <br>仅用于模版下载，兼容Mac</br>
 *
 */
@Data
public class ShiftOrgVerifyEntityForMac implements Serializable {
    private static final long serialVersionUID = 1L;

    //转移机构记录ID
    private Long shiftId;
    //用户登录账号
    @Excel(name = "登录账号", orderNum = "5", width = 50)
    private String loginCode;
    //转移机构推荐码
//    @Excel(name = "目标机构推荐码", orderNum = "8",isImportField = "serialCode", width = 20)
    private String serialCode;
    //创建时间
//    @Excel(name = "提交时间", orderNum = "9", width = 20, format = "YYYY-MM-dd HH:mm:ss")
    private Date createTime;
    //源机构编号
    @Excel(name = "源机构编号", orderNum = "6", width = 20)
    private String srcOrgCode;
    //目标机构编号
    @Excel(name = "目标机构编号", orderNum = "7", width = 20)
    private String tarOrgCode;
    //客户名称
    @Excel(name = "客户名称", orderNum = "3", isImportField = "userName", width = 20)
    private String userName;
    //客户手机号码
//    @Excel(name = "客户手机号码", orderNum = "4", isImportField = "phoneNumber", width = 20)
    private String phoneNumber;
    //主键ID
    private Long id;
    //审核类型；0：初审，1：复审，2：三审...
    private Integer type;
    //审核状态；-1：驳回，0：审核中，1：通过
    private Integer status;
//    @Excel(name = "审核结果", orderNum = "11", width = 20)
    private String statusSt;
    //是否需要下一步审核;0：否，1：是
    private boolean nextVerify;
    //审核时间
//    @Excel(name = "审核时间", orderNum = "10", width = 20, format = "YYYY-MM-dd HH:mm:ss")
    private Date verifyTime;
    //关联类型；REFUND_APPLY：退差价审核,...
    private String referType;
    //关联id
    private String referId;
    //审核人id
    private Long verifyBy;
    //备注
//    @Excel(name = "备注", orderNum = "11", width = 20)
    private String remark;

}

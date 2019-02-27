package com.dms.api.modules.entity.report.couponsReport;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 预付款券资金流水表
 */
@Data
public class CouponAmlog implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer amId; //流水号
    @Excel(name = "预订日", orderNum = "1", mergeVertical = false, isImportField = "tradingDay",width=25)
    private String tradingDay; //预订日
    private String couponSn; //券序列号
    @Excel(name = "划转金额", orderNum = "4", mergeVertical = false, isImportField = "amChange",width=25, numFormat = "0.00")
    private BigDecimal amChange; //资金划转金额
    @Excel(name = "资金划转类型", orderNum = "5", mergeVertical = false, isImportField = "type",width=25)
    private String type; //券流水类型【C001 服务商购买券  C002 券下单流水 C003 券下单剩余余额 C004 券失效划转,C005券退款划转】
    private BigDecimal desAftDrawable; //目的账户转账前余额
    private BigDecimal desBefDrawable; //目的账户转账后余额
    private BigDecimal srcAftDrawable; //源账户转账前余额
    private BigDecimal srcBefDrawable; //源账户转账后余额
    private String desAccId; //目标账户ID 转入账户id
    private String srcAccId; //源账户ID  转出账户id
    @Excel(name = "入库时间", orderNum = "6", mergeVertical = false, isImportField = "createTime",width=25)
    private String createTime; //入库时间
    @Excel(name = "完成时间", orderNum = "7", mergeVertical = false, isImportField = "completeDate",width=25)
    private String completeDate; //完成时间
    private String rmk1; //备注字段1
    private String rmk2; //备注字段2
    private String status; //状态【1:正常 -1:失败】
    private String remark; //备注
    @Excel(name = "服务商编号", orderNum = "2", mergeVertical = false, isImportField = "dealerCode",width=25)
    private String dealerCode; //服务商编号
    @Excel(name = "券批次号", orderNum = "3", mergeVertical = false, isImportField = "couponNo",width=25)
    private String couponNo; //券批次号

    public void setCreateTime(String createTime) {
        this.createTime = createTime.substring(0, createTime.lastIndexOf("."));
    }

    public void setCompleteDate(String completeDate) {
        this.completeDate = completeDate.substring(0, completeDate.lastIndexOf("."));
    }

}
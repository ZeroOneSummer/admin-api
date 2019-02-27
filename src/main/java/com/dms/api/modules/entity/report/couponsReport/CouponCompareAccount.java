package com.dms.api.modules.entity.report.couponsReport;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 券对账报表
 */
@Data
public class CouponCompareAccount implements Serializable {
    private static final long serialVersionUID = 1L;

    @Excel(name = "服务商编号", mergeVertical = false, isImportField = "dealerCode",width=25)
    private String dealerCode; //服务商编号
    @Excel(name = "券实际抵扣金额", mergeVertical = false, isImportField = "realUseMoney",width=25, numFormat = "0.00")
    private BigDecimal realUseMoney; //实用总额
    @Excel(name = "券退订退还金额", mergeVertical = false, isImportField = "backMoney",width=25, numFormat = "0.00")
    private BigDecimal backMoney; //退订退还总额
    @Excel(name = "券下单剩余金额", mergeVertical = false, isImportField = "orderMoney",width=25, numFormat = "0.00")
    private BigDecimal orderMoney; //券下单总余额
    @Excel(name = "货值增加", mergeVertical = false, isImportField = "profitClose",width=25, numFormat = "0.00")
    private BigDecimal profitClose; //货值增加
    @Excel(name = "货值减少", mergeVertical = false, isImportField = "profitCloseSub",width=25, numFormat = "0.00")
    private BigDecimal profitCloseSub; //货值减少
    @Excel(name = "失效劵总额", mergeVertical = false, isImportField = "overDueMoney",width=25, numFormat = "0.00")
    private BigDecimal overDueMoney; //失效劵总额
    @Excel(name = "服务费", mergeVertical = false, isImportField = "chargeOpen",width=25, numFormat = "0.00")
    private BigDecimal chargeOpen; //服务费
    @Excel(name = "K豆", mergeVertical = false, isImportField = "extraCharge",width=25, numFormat = "0.0000")
    private BigDecimal extraCharge; //K豆
    @Excel(name = "亏损金额", mergeVertical = false, isImportField = "lossMoney",width=25, numFormat = "0.00")
    private BigDecimal lossMoney; //亏损金额

}
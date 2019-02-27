package com.dms.api.modules.entity.report.couponsReport;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.hutool.core.date.DateUtil;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 预付款券订单表
 */
@Data
public class CouponOrder implements Serializable {
    private static final long serialVersionUID = 1L;

    @Excel(name = "服务商编号", isImportField = "dealerCode", width=25)
    private String dealerCode; //服务商编号
    @Excel(name = "登录账号", isImportField = "loginCode", width=25)
    private String loginCode; //登录账号
    @Excel(name = "券序列号", isImportField = "couponSn", width=25)
    private String couponSn; //券序列号
    @Excel(name = "订单号", isImportField = "orderNo", width=25)
    private String orderNo; //订单号
    @Excel(name = "退订单号", isImportField = "orderCloseNo", width=25)
    private String orderCloseNo; //退订单号
    @Excel(name = "券类型", isImportField = "couponType", width=25)
    private String couponType; //券类型【0-通用券(全额预付款券) 1-服务费券 2-预付款券】
    @Excel(name = "订单金额", isImportField = "orderMoney",width=25, numFormat = "0.00")
    private BigDecimal orderMoney; //订单金额
    @Excel(name = "券实际抵扣金额", isImportField = "orderCouponMoney",width=25, numFormat = "0.00")
    private BigDecimal orderCouponMoney; //券实际抵扣金额
    @Excel(name = "货值增加", isImportField = "profitClose",width=25, numFormat = "0.00")
    private BigDecimal profitClose; //退订货值增减
    @Excel(name = "货值减少", isImportField = "profitCloseSub",width=25, numFormat = "0.00")
    private BigDecimal profitCloseSub; //退订货值增减
    @Excel(name = "券退订退还金额", isImportField = "returnMoney",width=25, numFormat = "0.00")
    private BigDecimal returnMoney; //券退订退还金额
    @Excel(name = "券面额", isImportField = "price",width=25, numFormat = "0.00")
    private BigDecimal price; //券面额
    @Excel(name = "服务费", isImportField = "chargeOpen",width=25, numFormat = "0.00")
    private BigDecimal chargeOpen; //服务费
    @Excel(name = "K豆", isImportField = "extraCharge",width=25, numFormat = "0.0000")
    private BigDecimal extraCharge; //K豆
    @Excel(name = "创建时间", isImportField = "creDate", width=25)
    private String creDate; //创建时间
    @Excel(name = "划转次数", isImportField = "transferCount", width=25)
    private Integer transferCount; //划转次数
    @Excel(name = "划转时间", isImportField = "transferDate",width=25)
    private String transferDate; //划转时间

    public void setCreDate(Date creDate) {
        this.creDate = DateUtil.format(creDate, "yyyy-MM-dd HH:mm:ss");
    }

    public void setTransferDate(Date transferDate) {
        this.transferDate = DateUtil.format(transferDate, "yyyy-MM-dd HH:mm:ss");
    }
}
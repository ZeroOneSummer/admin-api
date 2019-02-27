package com.dms.api.modules.entity.reserve.coupons;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 预付款券资金流水表
 */
@Data
public class CouponAmountlog implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 券序列号
     */
    private String couponSn;

    /**
     * 资金划转金额
     */
    private BigDecimal amChange;

    /**
     * 资金流向（I = 收入， P = 支出）
     */
    private String activeType;

    /**
     * 预付款单号
     */
    private String orderCode;

    /**
     * 类型[下单使用/零散作废/失效划转/]
     */
    private Integer type;

    /**
     * 预订前余额
     */
    private BigDecimal amBefore;

    /**
     * 预订后余额
     */
    private BigDecimal amAfter;

    /**
     * 资金帐户id
     */
    private Integer accId;

    /**
     * 流水记录时间
     */
    private Date createTime;

    /**
     * 备注字段
     */
    private String remarks;


    /**
     * 设置券序列号
     *
     * @param couponSn 券序列号
     */
    public void setCouponSn(String couponSn) {
        this.couponSn = couponSn == null ? null : couponSn.trim();
    }


    /**
     * 设置资金流向（I = 收入， P = 支出）
     *
     * @param activeType 资金流向（I = 收入， P = 支出）
     */
    public void setActiveType(String activeType) {
        this.activeType = activeType == null ? null : activeType.trim();
    }


    /**
     * 设置预付款单号
     *
     * @param orderCode 预付款单号
     */
    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode == null ? null : orderCode.trim();
    }

    /**
     * 设置备注字段
     *
     * @param remarks 备注字段
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }
}
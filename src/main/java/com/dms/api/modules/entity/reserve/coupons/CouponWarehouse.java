package com.dms.api.modules.entity.reserve.coupons;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 预付款券仓库表
 */
@Data
public class CouponWarehouse implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String dealerCode;

    /**
     * 券序列号
     */
    private String couponSn;

    /**
     * 券批次号
     */
    private String couponNo;

    /**
     * 券面额
     */
    private BigDecimal price;

    /**
     * 券的密钥
     */
    private String couponPass;


    /**
     * 券最晚使用时间
     */
    private String couponUseEndDate;


    /**
     * 持有者 logincode
     */
    private String logincode;

    /**
     * 券状态[初始化状态/已领取/已使用/已销毁/已过期]
     */
    private String useStatus;

    /**
     * 券状态最后变更时间
     */
    private Date useTime;

    public CouponWarehouse() {
    }

    public CouponWarehouse(String couponSn, String couponNo, BigDecimal price, String couponPass, String useStatus) {
        this.couponSn = couponSn;
        this.couponNo = couponNo;
        this.price = price;
        this.couponPass = couponPass;
        this.useStatus = useStatus;
    }

    /**
     * 设置券序列号
     *
     * @param couponSn 券序列号
     */
    public void setCouponSn(String couponSn) {
        this.couponSn = couponSn == null ? null : couponSn.trim();
    }

    /**
     * 设置券编号
     *
     * @param couponNo 券编号
     */
    public void setCouponNo(String couponNo) {
        this.couponNo = couponNo == null ? null : couponNo.trim();
    }

    /**
     * 设置券的密钥
     *
     * @param couponPass 券的密钥
     */
    public void setCouponPass(String couponPass) {
        this.couponPass = couponPass == null ? null : couponPass.trim();
    }

    /**
     * 设置持有者 logincode
     *
     * @param logincode 持有者 logincode
     */
    public void setLogincode(String logincode) {
        this.logincode = logincode == null ? null : logincode.trim();
    }

}
package com.dms.api.modules.entity.reserve.coupons;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 预付款券使用记录表
 */
@Data
public class CouponUseLogs implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 券序列号
     */
    private String couponSn;

    /**
     * 使用方式[领取/使用/过期/销毁....]
     */
    private String useType;

    /**
     * 使用时间
     */
    private Date useTime;

    /**
     * 使用者
     */
    private String useUser;

    /**
     * 预付款单号
     */
    private String orderCode;

    /**
     * 备注信息
     */
    private String memo;

    /**
     * 设置券编号
     *
     * @param couponSn 券编号
     */
    public void setCouponSn(String couponSn) {
        this.couponSn = couponSn == null ? null : couponSn.trim();
    }


    /**
     * 设置使用方式[领取/使用/过期/销毁....]
     *
     * @param useType 使用方式[领取/使用/过期/销毁....]
     */
    public void setUseType(String useType) {
        this.useType = useType == null ? null : useType.trim();
    }


    /**
     * 设置使用者
     *
     * @param useUser 使用者
     */
    public void setUseUser(String useUser) {
        this.useUser = useUser == null ? null : useUser.trim();
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
     * 设置备注信息
     *
     * @param memo 备注信息
     */
    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }
}
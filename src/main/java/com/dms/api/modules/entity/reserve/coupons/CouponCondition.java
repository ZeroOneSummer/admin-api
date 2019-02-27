package com.dms.api.modules.entity.reserve.coupons;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 券限制条件表
 */
@Data
public class CouponCondition implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 券批次号
     */
    private String couponNo;

    /**
     * 类型  SEND:发放，USE:使用
     */
    private String type;

    /**
     * 条件类型   LEVEL:用户等级条件  WEIGHT:克重
     */
    private String conditionType;

    /**
     * 条件值
     */
    private String conditionValue;

    /**
     * 状态 有效:1 失效:0
     */
    private String status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 最后修改时间
     */
    private Date updateTime;

    /**
     * 修改人
     */
    private String updateUser;

    /**
     * 设置券批次号
     *
     * @param couponNo 券批次号
     */
    public void setCouponNo(String couponNo) {
        this.couponNo = couponNo == null ? null : couponNo.trim();
    }

    /**
     * 设置状态 有效:1 失效:0
     *
     * @param status 状态 有效:1 失效:0
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }


    /**
     * 设置创建人
     *
     * @param createUser 创建人
     */
    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    /**
     * 设置修改人
     *
     * @param updateUser 修改人
     */
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser == null ? null : updateUser.trim();
    }
}
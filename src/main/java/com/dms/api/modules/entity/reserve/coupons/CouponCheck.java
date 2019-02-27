package com.dms.api.modules.entity.reserve.coupons;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 券审核记录表
 */
@Data
public class CouponCheck implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id; //主键ID
    private String couponNo; //券批次号
    private String status; //审核状态 通过:1 驳回:0.5 不同意:-1
    private String rmk; //审核意见
    private Date createTime; //创建日期
    private Date updateTime; //审核时间
    private String updateUser; //修改人
    private String loginPwd; //登录密码
    private String payPwd; //支付密码
    private String endDate; //截止日期

}




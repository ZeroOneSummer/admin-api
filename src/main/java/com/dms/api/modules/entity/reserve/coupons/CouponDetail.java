package com.dms.api.modules.entity.reserve.coupons;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 预付款券基础信息表
 */
@Data
public class CouponDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 券批次号
     */
    private String couponNo;

    /**
     * 券名称
     */
    private String couponName;

    /**
     * 券状态 有效:1 失效:0
     */
    private String status;

    /**
     * 券类型（0:通用券  1:服务费券 2:预付款券/）
     */
    private String couponType;

    /**
     * 服务商ID（券归属服务商）
     */
    private String dealerId;

    /**
     * 券总额（面额*券发放数量）
     */
    private BigDecimal totalPrice;

    /**
     * 券面额
     */
    private BigDecimal price;

    /**
     * 满多少可用
     */
    private BigDecimal fullPrice;

    /**
     * 券生效起始时间
     */
    private String startDate;

    /**
     * 券有效截止时间
     */
    private String endDate;

    /**
     * 券领取后有多少天失效
     */
    private String couponGetInvalidDay;

    /**
     * 券初始化数量
     */
    private Integer couponNumber;

    /**
     * 消息通知方式[短信/站内信/...]
     */
    private String messageType;

    /**
     * 自定义券封面图片
     */
    private String photo;

    /**
     * 券描述信息
     */
    private String description;

    /**
     * 获取方式（GET=用户领取，SEND=系统发放）
     */
    private String obtainType;

    /**
     * 每人获取次数上限
     */
    private Integer obtainLimit;

    /**
     * 发券场景[ex: 1:注册发放 2:实名发放 3:升级商家等级发放...]
     */
    private String sendType;

    /**
     * 领取场景[1:积分兑换 2:密钥兑换 3:免费领取...]  第一阶段不考虑
     */
    private String getType;

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

    //服务商登录密码
    private String loginPwd;

    //购买券支付密码
    private String payPwd;


    private String couponSn;

    private String dealerName;

    /**
     * 设置券编号/批次号
     *
     * @param couponNo 券编号/批次号
     */
    public void setCouponNo(String couponNo) {
        this.couponNo = couponNo == null ? null : couponNo.trim();
    }

    /**
     * 设置券名称
     *
     * @param couponName 券名称
     */
    public void setCouponName(String couponName) {
        this.couponName = couponName == null ? null : couponName.trim();
    }

    /**
     * 设置券类型（0:通用券  1:服务费券 2:预付款券/）
     *
     * @param couponType 券类型（0:通用券  1:服务费券 2:预付款券/）
     */
    public void setCouponType(String couponType) {
        this.couponType = couponType == null ? null : couponType.trim();
    }


    /**
     * 设置服务商编号（券归属服务商）
     *
     * @param dealerId 服务商ID（券归属服务商）
     */
    public void setDealerId(String dealerId) {
        this.dealerId = dealerId == null ? null : dealerId.trim();
    }

    /**
     * 设置消息通知方式[短信/站内信/...]
     *
     * @param messageType 消息通知方式[短信/站内信/...]
     */
    public void setMessageType(String messageType) {
        this.messageType = messageType == null ? null : messageType.trim();
    }

    /**
     * 设置自定义券封面图片
     *
     * @param photo 自定义券封面图片
     */
    public void setPhoto(String photo) {
        this.photo = photo == null ? null : photo.trim();
    }

    /**
     * 设置券描述信息
     *
     * @param description 券描述信息
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    /**
     * 设置获取方式（GET=用户领取，SEND=系统发放）
     *
     * @param obtainType 获取方式（GET=用户领取，SEND=系统发放）
     */
    public void setObtainType(String obtainType) {
        this.obtainType = obtainType == null ? null : obtainType.trim();
    }

    /**
     * 设置发券场景[ex: 1:注册发放 2:实名发放 3:升级商家等级发放...]
     *
     * @param sendType 发券场景[ex: 1:注册发放 2:实名发放 3:升级商家等级发放...]
     */
    public void setSendType(String sendType) {
        this.sendType = sendType == null ? null : sendType.trim();
    }

    /**
     * 设置领取场景[1:积分兑换 2:密钥兑换 3:免费领取...]  第一阶段不考虑
     *
     * @param getType 领取场景[1:积分兑换 2:密钥兑换 3:免费领取...]  第一阶段不考虑
     */
    public void setGetType(String getType) {
        this.getType = getType == null ? null : getType.trim();
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

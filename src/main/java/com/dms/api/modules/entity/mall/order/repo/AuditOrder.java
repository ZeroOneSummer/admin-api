package com.dms.api.modules.entity.mall.order.repo;

import lombok.Data;

import java.util.Date;

/**
 * @author Administrator
 * @Description: 我的审批订单
 * @date 2018/9/7 09:40
 */
@Data
public class AuditOrder {

    private String applyUser; //审批发起人
    private String orderNo; //订单号
    private boolean isRequest; //是否需要回调true&false
    private Date createTime; //创建时间
    private String name; //审核组名称
    private String amt; //回购金额
    private String id; //审核订单ID
    private String requestNo; //请求编号
    private Date applyTime; //审核时间
    private boolean applyStatus; //审核状态true&false
    private String productName; //产品名称
    private String userUid; //签约席位号
    private String loginCode; //用户标识
    private String couponValue; //券金额
    private String goldenBean; //金贝
    private String purchasPrice; //购买总金额
    private String payTYpe; //支付类型： 线上-ONLINEL 线下-OFFLINE
    private String bankAccountName; //账号行名称
    private String bankAccountNo; //银行卡号
    private String branchBankAccountName; //账号分行名称
    private String mall; //商城名称 KKG OR KKA OR SSMALL


    public void setCreateTime(Long createTime) {
        this.createTime = new Date(createTime);
    }

    public void setApplyTime(Long applyTime) {
        this.applyTime = new Date(applyTime);
    }
}

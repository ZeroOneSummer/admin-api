package com.dms.api.modules.entity.report.bean;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 40
 * date 2017/12/23
 * time 14:36
 * decription:40
 */
@Data
public class BBillEntity {
    //id
    private Long id;

    private Long accId;

    //所属商家id
    private Long dealerId;

    //所属机构id
    private Long orgId;

    //本月即将过期K豆
    private BigDecimal valalidBounty;

    //当前剩余K豆总额
    private BigDecimal surplusBounty;

    //本期收充值豆总额
    private BigDecimal bountyIn;

    //本期支提现豆总额
    private BigDecimal bountyPay;

    //账单是否有效
    private boolean isValid;
}

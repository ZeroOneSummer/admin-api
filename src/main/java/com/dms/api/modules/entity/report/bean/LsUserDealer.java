package com.dms.api.modules.entity.report.bean;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 40
 * date 2017/12/12
 * time 15:28
 * decription:商城用户关联商家
 */
@Data
public class LsUserDealer {

    private String userId;

    private String accId;

    private String dealerId;

    private BigDecimal bounty;
}

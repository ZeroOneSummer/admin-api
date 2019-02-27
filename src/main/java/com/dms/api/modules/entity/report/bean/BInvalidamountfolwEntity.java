package com.dms.api.modules.entity.report.bean;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 40
 * date 2017/12/23
 * time 14:55
 * decription:
 */
@Data
public class BInvalidamountfolwEntity {

    //id
    private long id;

    //账单id
    private long billId;

    //失效时间
    private Date failureTime;

    //失效金额
    private BigDecimal invalidAmount;

    //付款acc_id
    private Long paymentAccId;

    //收款acc_id
    private Long receivablesAccId;

    //资金账户id
    private Long accId;

    //是否划转
    private boolean status;

    //所属服务商id
    private Long dealerId;

    //所属机构id
    private Long orgId;

}

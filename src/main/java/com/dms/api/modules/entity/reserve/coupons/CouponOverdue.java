package com.dms.api.modules.entity.reserve.coupons;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 预付款券使用记录表
 */
@Data
public class CouponOverdue implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    //券序列号
    private String couponSn;
    //失效金额
    private BigDecimal overdueMoney;
    //失效时间
    private Date creDate;
    //是否已划转 1:是 0否
    private String hasTransfer;
    //失效类型(-1销毁,-2过期失效 -3下单剩余金额)
    private String type;
    //划转时间
    private Date transferDate;

}
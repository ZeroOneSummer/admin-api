package com.dms.api.modules.entity.report.bean;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 40
 * date 2018/1/16
 * time 12:02
 * decription:用作K豆日结统计
 */
@Data
public class TypeSettlementEntity {

    //预订日
    private String tradingDay;
    //预订金额
    private BigDecimal price;
    //预订类型
    private String type;
    //此预订日之前的总额，包含当前预订日
    private BigDecimal total;
}

package com.dms.api.modules.entity.report.mallrun;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商城运营信息
 */
@Data
public class MallRunEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id; //ID
//    @Excel(name = "服务商ID", mergeVertical = false, isImportField = "dealerId", width=25)
    private String dealerId; //服务商ID
    @Excel(name = "服务商编号", mergeVertical = false, isImportField = "dealerCode", width=25)
    private String dealerCode; //服务商编号
    @Excel(name = "统计类型", mergeVertical = false, isImportField = "type",width=25)
    private String type; //统计类型【服务费-FW，充值-CZ，提现-TX，货值增减-HZ，注册用户数量-ZC，下单用户数量-XD，实名用户数量-SM】
//    @Excel(name = "结算类型", mergeVertical = false, isImportField = "timeType", width=25)
    private String timeType; //结算类型【D-日，M-月，Y-年】
    @Excel(name = "数值", mergeVertical = false, isImportField = "price", width=25, numFormat = "0.00")
    private BigDecimal price; //数值
    @Excel(name = "累加数值", mergeVertical = false, isImportField = "accumPrice", width=25, numFormat = "0.00")
    private BigDecimal accumPrice; //累加数值
    @Excel(name = "结算日", mergeVertical = false, isImportField = "traDay", width=25)
    private String traDay; //结算日
//    @Excel(name = "结算月", mergeVertical = false, isImportField = "traMon", width=25)
    private String traMon; //结算月
//    @Excel(name = "创建时间", mergeVertical = false, isImportField = "creTime", width=25)
    private String creTime; //创建时间

}
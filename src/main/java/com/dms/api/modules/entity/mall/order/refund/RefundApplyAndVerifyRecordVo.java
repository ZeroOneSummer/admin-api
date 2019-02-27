package com.dms.api.modules.entity.mall.order.refund;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class RefundApplyAndVerifyRecordVo {

    //主键ID
    private Long id;
    //全额订单单号
    @Excel(name = "订单号", orderNum = "1", mergeVertical = false, isImportField = "orderCode",width=25)
    private String orderCode;
    //用户姓名
    @Excel(name = "用户姓名", orderNum = "2", mergeVertical = false, isImportField = "userName",width=20)
    private String userName;
    //订单金额
    @Excel(name = "订单金额", orderNum = "6", mergeVertical = false, isImportField = "orderPrice",width=20)
    private BigDecimal orderPrice;
    //退差价金额
    @Excel(name = "退差价额", orderNum = "8", mergeVertical = false, isImportField = "refundMoney",width=20)
    private BigDecimal refundMoney;
    //退差K豆数量
    @Excel(name = "退差K豆数量", orderNum = "9", mergeVertical = false, isImportField = "refundBean",width=20)
    private BigDecimal refundBean;
    //退差价总额
    @Excel(name = "退差价总额", orderNum = "7", mergeVertical = false, isImportField = "refundTotal",width=20)
    private BigDecimal refundTotal;
    //申请时间
    @Excel(name = "申请时间", orderNum = "10", mergeVertical = false, isImportField = "applyTime",width=30 ,format ="yyyy-MM-dd HH:mm:ss")
    private Date applyTime;
    //备注信息
    @Excel(name = "备注信息", orderNum = "12", mergeVertical = false, isImportField = "remarks",width=50)
    private String remarks;
    //审核类型；0：初审，1：复审，2：三审...
    @Excel(name = "审核类型", orderNum = "3", mergeVertical = false, isImportField = "type",width=20 ,replace ={"初审_0","复审_1","三审_2"})
    private Integer type;
    //审核状态；-1：驳回，0：审核中，1：通过
    @Excel(name = "审核状态", orderNum = "4", mergeVertical = false, isImportField = "status",width=20 ,replace ={"驳回_-1","审核中_0","通过_1"})
    private Integer status;
    //审核时间
    @Excel(name = "审核时间", orderNum = "11", mergeVertical = false, isImportField = "verifyTime",width=30 ,format ="yyyy-MM-dd HH:mm:ss")
    private Date verifyTime;

    private Long vId;
    //审核人
    @Excel(name = "审核人", orderNum = "5", mergeVertical = false, isImportField = "verifyName",width=20)
    private String verifyName;

    private boolean nextVerify;

}

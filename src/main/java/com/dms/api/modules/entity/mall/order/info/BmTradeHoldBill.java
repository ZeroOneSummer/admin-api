package com.dms.api.modules.entity.mall.order.info;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 
 */
@Data
public class BmTradeHoldBill implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    private Long id;

    /**
     * 资金账户id
     */
    private String accId;

    /**
     * 推荐费
     */
    private BigDecimal chargeReferr;

    /**
     * 占用预付款
     */
    private BigDecimal marginUsed;

    /**
     * 预付款比例
     */
    private Float marginUsedCalc;

    /**
     * 预定价
     */
    private BigDecimal priceOpen;

    /**
     * 持仓数量
     */
    private Integer quantityHold;

    /**
     * 商品附加数据2
     */
    private String skustr;

    /**
     * 预付款计提方式
     */
    private String marginCalculetType;

    /**
     * 预定服务费
     */
    private BigDecimal chargeOpen;

    /**
     * 商品附加数据
     */
    private String sku;

    /**
     * 委托单号
     */
    private String orderCode;

    /**
     * 持仓价
     */
    private BigDecimal priceHold;

    /**
     * 商品id
     */
    private Long symbolId;

    /**
     * 交收预付款
     */
    private BigDecimal deliveryMargin;

    /**
     * 交收状态
     */
    private String deliveryStatus;

    /**
     * 止损价
     */
    private BigDecimal priceStopLose;

    /**
     * ??? 
     */
    private BigDecimal dailySProfit;

    /**
     * 合约大小
     */
    private Double contractSize;

    /**
     * 结算货值增减
     */
    private BigDecimal settlementProfit;

    /**
     * 延迟提货费
     */
    private BigDecimal chargeInterest;

    /**
     * 止盈价
     */
    private BigDecimal priceTakeProfit;

    /**
     * 预定时间
     */
    private String openDate;

    /**
     * 合约编号
     */
    private Long contractId;

    /**
     * 交收申请时间
     */
    private String deliveryTime;

    /**
     * 上日的延迟提货费
     */
    private BigDecimal dailyChargeInterest;

    /**
     * 买卖方向
     */
    private String bsCode;

    private String extraAccoutId;

    /**
     * ?????
     */
    private BigDecimal extraCharge;

    /**
     * 备注
     */
    private String remarks;

    private String userName;
    private String orderCodeRe;
    private String closeDate;
    private Float priceClose;
    private Float profitClose;
    private String dealerCode;
    private String dealerName;
    private String orgName;
    private String symbolName;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", accId=").append(accId);
        sb.append(", chargeReferr=").append(chargeReferr);
        sb.append(", marginUsed=").append(marginUsed);
        sb.append(", marginUsedCalc=").append(marginUsedCalc);
        sb.append(", priceOpen=").append(priceOpen);
        sb.append(", quantityHold=").append(quantityHold);
        sb.append(", skustr=").append(skustr);
        sb.append(", marginCalculetType=").append(marginCalculetType);
        sb.append(", chargeOpen=").append(chargeOpen);
        sb.append(", sku=").append(sku);
        sb.append(", orderCode=").append(orderCode);
        sb.append(", priceHold=").append(priceHold);
        sb.append(", symbolId=").append(symbolId);
        sb.append(", deliveryMargin=").append(deliveryMargin);
        sb.append(", deliveryStatus=").append(deliveryStatus);
        sb.append(", priceStopLose=").append(priceStopLose);
        sb.append(", dailySProfit=").append(dailySProfit);
        sb.append(", contractSize=").append(contractSize);
        sb.append(", settlementProfit=").append(settlementProfit);
        sb.append(", chargeInterest=").append(chargeInterest);
        sb.append(", priceTakeProfit=").append(priceTakeProfit);
        sb.append(", openDate=").append(openDate);
        sb.append(", contractId=").append(contractId);
        sb.append(", deliveryTime=").append(deliveryTime);
        sb.append(", dailyChargeInterest=").append(dailyChargeInterest);
        sb.append(", bsCode=").append(bsCode);
        sb.append(", extraAccoutId=").append(extraAccoutId);
        sb.append(", extraCharge=").append(extraCharge);
        sb.append(", remarks=").append(remarks);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
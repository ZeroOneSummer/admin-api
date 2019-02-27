package com.dms.api.modules.entity.mall.order.info;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 
 */
@Data
public class BmTradeOrder implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    private Long id;

    /**
     * 资金账户Id
     */
    private String accId;

    /**
     * 委托单号 primaryKeyName
     */
    private String orderCode;

    /**
     * 退订价格
     */
    private BigDecimal openHoldPrice;

    /**
     * 推荐费
     */
    private BigDecimal chargeReferr;

    /**
     * 退订货值增减
     */
    private BigDecimal profitClose;

    /**
     * 占用预付款
     */
    private BigDecimal marginUsed;

    /**
     * 预订市场Id
     */
    private Long tradeMarketId;

    /**
     * 预付款比例
     */
    private Float marginUsedCalc;

    /**
     * 礼券下单标志
     */
    private String useCoupon;

    /**
     * 预定价格
     */
    private BigDecimal openPrice;

    /**
     * 委托单状态
     */
    private String orderStatus;

    /**
     * 预付款计提方式
     */
    private String marginCalculetType;

    /**
     * 预定服务费
     */
    private Long chargeOpen;

    /**
     * ???
     */
    private Integer quotedDecimal;

    /**
     * 止盈价
     */
    private BigDecimal priceTakeProfit;

    /**
     * 微预订订购数量
     */
    private Integer binaryQuantity;

    /**
     * 已平数量
     */
    private Integer closedQuantity;

    /**
     * 止盈价差
     */
    private Integer pointTakeProfit;

    /**
     * 商品ID
     */
    private String symbolId;

    /**
     * 交收预付款
     */
    private BigDecimal deliveryMargin;

    /**
     * 委托价格
     */
    private BigDecimal orderPrice;

    /**
     * 成交金额
     */
    private BigDecimal completeAmount;

    /**
     * 交收状态
     */
    private String deliveryStatus;

    /**
     * 预订日
     */
    private String tradingDay;

    /**
     * 委托数量
     */
    private Integer orderQuantity;

    /**
     * 微预订模式
     */
    private String binaryMode;

    /**
     * 成交时间
     */
    private String dealTime;

    /**
     * 单据有效期类型
     */
    private String validDateType;

    /**
     * 上日结算货值增减
     */
    private BigDecimal dailySProfit;

    /**
     * 商品附加数据
     */
    private String sku;

    /**
     * 委托时间
     */
    private String orderTime;

    /**
     * 卖价差
     */
    private Integer pointBid;

    /**
     * 合约大小
     */
    private Double contractSize;

    /**
     * 冻结预付款
     */
    private BigDecimal marginFreezed;

    /**
     * 单据有效期
     */
    private String validDate;

    /**
     * ???
     */
    private String binaryWinFlag;

    /**
     * 持仓价
     */
    private BigDecimal holdPrice;

    /**
     * 偏离价差
     */
    private Integer pointOffset;

    /**
     * 买价差
     */
    private Integer pointAsk;

    /**
     * 结算货值增减
     */
    private BigDecimal settlementProfit;

    /**
     * 延迟提货费
     */
    private BigDecimal chargeInterest;

    /**
     * 价差收益
     */
    private BigDecimal profitPoint;

    /**
     * 价格来源
     */
    private String priceSource;

    /**
     * 分组编号
     */
    private String groupId;

    /**
     * 合约编号
     */
    private Long contractId;

    /**
     * 商家Id
     */
    private String dealerId;

    /**
     * 交收申请时间
     */
    private String deliveryTime;

    /**
     * 上日的延迟提货费
     */
    private BigDecimal dailyChargeInterest;

    /**
     * 止损价
     */
    private BigDecimal priceStopLose;

    /**
     * 止损价差
     */
    private Integer pointStopLose;

    /**
     * 成交数量
     */
    private BigDecimal completeQuantity;

    /**
     * 关联单号
     */
    private String orderCodeRe;

    /**
     * 委托单类型
     */
    private String orderType;

    /**
     * 退订服务费
     */
    private BigDecimal chargeClose;

    /**
     * 委托单方向
     */
    private String bsCode;

    /**
     * 商品附加数据2 下单接口新加字段skustr,字符串，允许字母数字冒号逗号和点（含有其他字符的话，下单失败），长度最大为64字节，超出部分会被截断
     */
    private String skustr;

    /**
     * 奖励K豆
     */
    private BigDecimal extraCharge;

    /**
     * 奖励K豆划转账户accid
     */
    private Integer extraAccoutId;
    /**
     * 备注
     */
    private String remarks;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", accId=").append(accId);
        sb.append(", orderCode=").append(orderCode);
        sb.append(", openHoldPrice=").append(openHoldPrice);
        sb.append(", chargeReferr=").append(chargeReferr);
        sb.append(", profitClose=").append(profitClose);
        sb.append(", marginUsed=").append(marginUsed);
        sb.append(", tradeMarketId=").append(tradeMarketId);
        sb.append(", marginUsedCalc=").append(marginUsedCalc);
        sb.append(", useCoupon=").append(useCoupon);
        sb.append(", openPrice=").append(openPrice);
        sb.append(", orderStatus=").append(orderStatus);
        sb.append(", marginCalculetType=").append(marginCalculetType);
        sb.append(", chargeOpen=").append(chargeOpen);
        sb.append(", quotedDecimal=").append(quotedDecimal);
        sb.append(", priceTakeProfit=").append(priceTakeProfit);
        sb.append(", binaryQuantity=").append(binaryQuantity);
        sb.append(", closedQuantity=").append(closedQuantity);
        sb.append(", pointTakeProfit=").append(pointTakeProfit);
        sb.append(", symbolId=").append(symbolId);
        sb.append(", deliveryMargin=").append(deliveryMargin);
        sb.append(", orderPrice=").append(orderPrice);
        sb.append(", completeAmount=").append(completeAmount);
        sb.append(", deliveryStatus=").append(deliveryStatus);
        sb.append(", tradingDay=").append(tradingDay);
        sb.append(", orderQuantity=").append(orderQuantity);
        sb.append(", binaryMode=").append(binaryMode);
        sb.append(", dealTime=").append(dealTime);
        sb.append(", validDateType=").append(validDateType);
        sb.append(", dailySProfit=").append(dailySProfit);
        sb.append(", sku=").append(sku);
        sb.append(", orderTime=").append(orderTime);
        sb.append(", pointBid=").append(pointBid);
        sb.append(", contractSize=").append(contractSize);
        sb.append(", marginFreezed=").append(marginFreezed);
        sb.append(", validDate=").append(validDate);
        sb.append(", binaryWinFlag=").append(binaryWinFlag);
        sb.append(", holdPrice=").append(holdPrice);
        sb.append(", pointOffset=").append(pointOffset);
        sb.append(", pointAsk=").append(pointAsk);
        sb.append(", settlementProfit=").append(settlementProfit);
        sb.append(", chargeInterest=").append(chargeInterest);
        sb.append(", profitPoint=").append(profitPoint);
        sb.append(", priceSource=").append(priceSource);
        sb.append(", groupId=").append(groupId);
        sb.append(", contractId=").append(contractId);
        sb.append(", dealerId=").append(dealerId);
        sb.append(", deliveryTime=").append(deliveryTime);
        sb.append(", dailyChargeInterest=").append(dailyChargeInterest);
        sb.append(", priceStopLose=").append(priceStopLose);
        sb.append(", pointStopLose=").append(pointStopLose);
        sb.append(", completeQuantity=").append(completeQuantity);
        sb.append(", orderCodeRe=").append(orderCodeRe);
        sb.append(", orderType=").append(orderType);
        sb.append(", chargeClose=").append(chargeClose);
        sb.append(", bsCode=").append(bsCode);
        sb.append(", skustr=").append(skustr);
        sb.append(", extraCharge=").append(extraCharge);
        sb.append(", extraAccoutId=").append(extraAccoutId);
        sb.append(", remarks=").append(remarks);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
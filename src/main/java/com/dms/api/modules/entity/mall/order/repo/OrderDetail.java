package com.dms.api.modules.entity.mall.order.repo;

import cn.hutool.core.date.DateUtil;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author jp
 * @Description: 审批的订单详情
 * @date 2018/9/10 11:25
 */
@Data
public class OrderDetail {

    private Integer subId; //订购ID
    private String subNumber; //订购流水号
    private String prodName; //产品名称
    private Integer productNums; //订单商品总数
    private Integer orderType; //订单类型 1-预付款单 2-全额单
    private String userName; //订购用户名称
    private String serverNum; //服务商编号
    private BigDecimal total; //总值
    private BigDecimal actualTotal; //实际总值
    private Integer score; //使用积分
    private BigDecimal discountPrice; //优惠总金额
    private BigDecimal priceRefund; //退差价金额
    private String subDate; //订购时间
    private String payDate; //购买时间
    private String finallyDate; //完成时间
    private String subType; //全款单类型【NORMAL-普通订单 AUCTIONS-拍卖订单 GROUP-团购订单 SHOP_STORE-门店订单 SECKILL-秒杀订单】


    public void setSubDate(Long subDate) {
        this.subDate = DateUtil.format(new Date(subDate), "yyyy-MM-dd HH:mm:ss");
    }

    public void setPayDate(Long payDate) {
        this.payDate = DateUtil.format(new Date(payDate), "yyyy-MM-dd HH:mm:ss");
    }

    public void setFinallyDate(Long finallyDate) {
        this.finallyDate = DateUtil.format(new Date(finallyDate), "yyyy-MM-dd HH:mm:ss");
    }

}

package com.dms.api.common.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/7/24.
 */
public class ParamMapUtils {
    public static final Map<String,String> beanTypeMap=new HashMap<>();
    public static final Map<String,String> amTypeMap=new HashMap<>();
    public static final Map<String,String> statusMap=new HashMap<>();
    public static final Map<String,String> bsCodeMap=new HashMap<>();
    public static final Map<String,String> closeTypeMap=new HashMap<>();
    public static final Map<String,String> orderTypeMap=new HashMap<>();
    public static final Map<String,String> couponsLogTypeMap=new HashMap<>();
    public static final Map<String,String> couponOrderTypeMap=new HashMap<>();
    public static final Map<String,String> mallRunQueryTypeMap=new HashMap<>();

    static{
        beanTypeMap.put("kkg", "K豆");
        beanTypeMap.put("kka", "K豆");
        beanTypeMap.put("ssm", "金贝");
        beanTypeMap.put("jy", "即遇星");
        beanTypeMap.put("dj", "金点");

        amTypeMap.put("1","充值");
        amTypeMap.put("2","提现");
        amTypeMap.put("3","调整");
        amTypeMap.put("4","预定服务费");
        amTypeMap.put("5","退订服务费");
        amTypeMap.put("6","退订货值增减");
        amTypeMap.put("7","结算货值增减");
        amTypeMap.put("8","结算计息");
        amTypeMap.put("9","冻结");
        amTypeMap.put("A","回退冻结");
        amTypeMap.put("B","占用");
        amTypeMap.put("C","回退占用");
        amTypeMap.put("D","充值服务费");
        amTypeMap.put("E","提现服务费");
        amTypeMap.put("F","服务费结转");
        amTypeMap.put("G","交割服务费");
        amTypeMap.put("H","交割金额");
        amTypeMap.put("X","红冲");
        amTypeMap.put("Z","蓝补");
        amTypeMap.put("I","提现回退");
        amTypeMap.put("J","减损回退");
        amTypeMap.put("K","价差收益");
        amTypeMap.put("L","预订管理费");
        amTypeMap.put("M","划入");
        amTypeMap.put("N","划出");
        amTypeMap.put("R","返息");
        amTypeMap.put("O","补偿的结算计息");
        amTypeMap.put("P","订货分部退订货值增减");
        amTypeMap.put("Q","订货分部服务费结转");
        amTypeMap.put("T","担保金额变动");
        amTypeMap.put("V","移仓商家补偿");
        amTypeMap.put("U","交收违约金");
        amTypeMap.put("W","获得交收违约金");
        amTypeMap.put("Y","预订转单货值增减修正");
        amTypeMap.put("a","货值增减服务费修正");
        amTypeMap.put("b","仓租费");
        amTypeMap.put("c","融货服务费");
        amTypeMap.put("d","申购服务费");
        amTypeMap.put("e","申购冻结款");
        amTypeMap.put("f","申购冻结款退还");
        amTypeMap.put("g","发售库存款");
        amTypeMap.put("h","发售预定");
        amTypeMap.put("i","发售退订");
        amTypeMap.put("j","发售转让");
        amTypeMap.put("k","交收服务费退还");
        amTypeMap.put("l","付出推荐费");
        amTypeMap.put("m","推荐费收益");
        amTypeMap.put("n","预订违约金");
        amTypeMap.put("o","预订商品收益");
        amTypeMap.put("p","运营中心返佣");
        amTypeMap.put("q","推荐费返佣");
        amTypeMap.put("r","推荐费转单补偿");
        amTypeMap.put("s","系统外清算");
        amTypeMap.put("t","礼券增益");
        amTypeMap.put("u","预定服务费转单补偿");
        amTypeMap.put("v","退订服务费转单补偿");
        amTypeMap.put("w","管理费转单补偿");

        statusMap.put("1","待审");
        statusMap.put("2","审核中");
        statusMap.put("3","已批准");
        statusMap.put("4","已驳回");
        statusMap.put("5","已撤销");

        bsCodeMap.put("b","预付款");
        bsCodeMap.put("s","预约回收");
        bsCodeMap.put("","-");

        closeTypeMap.put("1","止损退订");
        closeTypeMap.put("2","止盈退订");
        closeTypeMap.put("3","市价退订");
        closeTypeMap.put("4","系统退订");
        closeTypeMap.put("5","市价转让");
        closeTypeMap.put("6","限价转让");
        closeTypeMap.put("7","熔断退订");
        closeTypeMap.put("8","换月退订");
        closeTypeMap.put("9","风控退订");
        closeTypeMap.put("A","交收退订");
        closeTypeMap.put("B","商品过夜退订");
        closeTypeMap.put("","-");

        orderTypeMap.put("1","市价预订单");
        orderTypeMap.put("2","市价退订单");
        orderTypeMap.put("3","限价止限单");
        orderTypeMap.put("4","限价停损单");
        orderTypeMap.put("5","限价退订单");
        orderTypeMap.put("6","撤销委托");
        orderTypeMap.put("7","修改单据");
        orderTypeMap.put("8","强制退订");
        orderTypeMap.put("9","系统撤单");
        orderTypeMap.put("A","委托单");
        orderTypeMap.put("B","商品过夜退订(指定价)");
        orderTypeMap.put("C","委托单(最优价)");
        orderTypeMap.put("D","转让单(指定价)");
        orderTypeMap.put("E","转让单(最优价)");
        orderTypeMap.put("F","微预订订单");
        orderTypeMap.put("G","临时盘点被动退订单");
        orderTypeMap.put("H","换月盘点被动退订单");
        orderTypeMap.put("I","风控强平");
        orderTypeMap.put("J","交收强平");
        orderTypeMap.put("L","过夜盘点被动退订单");
        orderTypeMap.put("","-");

        couponsLogTypeMap.put("C001","服务商购买券");
        couponsLogTypeMap.put("C002","券下单流水");
        couponsLogTypeMap.put("C003","券下单剩余余额");
        couponsLogTypeMap.put("C004","券失效划转");
        couponsLogTypeMap.put("C005","券退款划转");

        couponOrderTypeMap.put("0","全额预付款券");
        couponOrderTypeMap.put("1","服务费券");
        couponOrderTypeMap.put("2","预付款券");

        mallRunQueryTypeMap.put("FW","服务费");
        mallRunQueryTypeMap.put("CZ","充值");
        mallRunQueryTypeMap.put("TX","提现");
        mallRunQueryTypeMap.put("HZ","货值增减");
        mallRunQueryTypeMap.put("ZC","注册用户数量");
        mallRunQueryTypeMap.put("XD","下单用户数量");
        mallRunQueryTypeMap.put("SM","实名用户数量");

    }

}

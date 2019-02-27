package com.dms.api.common.Enum;

public enum FieldEnum {

    //用户等级
    LEVEL0_5("0.5","体验用户","0.5"),
    LEVEL0("0","普通用户","0"),
    LEVEL1("1","一级商家","1"),
    LEVEL2("2","二级商家","2"),
    LEVEL3("3","三级商家","3"),


    //资金账户状态
    ACCSTATUS1("1","正常",""),
    ACCSTATUS2("2","冻结",""),
    ACCSTATUS3("3","受限",""),
    ACCSTATUS4("4","销户",""),
    ACCSTATUS5("5","异常",""),
    ACCSTATUS6("6","未激活",""),
    ACCSTATUS7("7","可激活",""),

    //资金账户表accType
    ACCTYPE1("1","客户",""),
    ACCTYPE2("2","机构客户",""),
    ACCTYPE3("3","订货分部",""),
    ACCTYPE4("4","订货部",""),
    ACCTYPE5("5","区域服务商",""),

    //资金流水amType
    AMTYPE1("1"	,"充值"	,"TYPE_1"),
    AMTYPE2("2"	,"提现"	,"TYPE_2"),
    AMTYPE3("3"	,"调整"	,"TYPE_3"),
    AMTYPE4("4"	,"服务费"	,"TYPE_4"),
    AMTYPE5("5"	,"退订单服务费"	,"TYPE_5"),
    AMTYPE6("6"	,"退订单货值增减"	,"TYPE_6"),
    AMTYPE7("7"	,"结算货值增减"	,"TYPE_7"),
    AMTYPE8("8"	,"结算计息"	,"TYPE_8"),
    AMTYPE9("9"	,"冻结"	,"TYPE_9"),
    AMTYPEA("A"	,"回退冻结"	,"TYPE_A"),
    AMTYPEB("B"	,"占用"	,"TYPE_B"),
    AMTYPEC("C"	,"回退占用"	,"TYPE_C"),
    AMTYPED("D"	,"充值服务费"	,"TYPE_D"),
    AMTYPEE("E"	,"提现服务费"	,"TYPE_E"),
    AMTYPEF("F"	,"服务费结转"	,"TYPE_F"),
    AMTYPEG("G"	,"提货服务费"	,"TYPE_G"),
    AMTYPEH("H"	,"提货金额"	,"TYPE_H"),
    AMTYPEX("X"	,"红冲"	,"TYPE_X"),
    AMTYPEZ("Z"	,"蓝补"	,"TYPE_Z"),
    AMTYPEI("I"	,"提现回退"	,"TYPE_I"),
    AMTYPEJ("J"	,"减损回退"	,"TYPE_J"),
    AMTYPEK("K"	,"价差收益"	,"TYPE_K"),
    AMTYPEL("L"	,"预订货管理费"	,"TYPE_L"),
    AMTYPEM("M"	,"划入"	,"TYPE_M"),
    AMTYPEN("N"	,"划出"	,"TYPE_N"),
    AMTYPER("R"	,"返息"	,"TYPE_R"),
    AMTYPEO("O"	,"补偿的结算计息"	,"TYPE_O"),
    AMTYPEP("P"	,"订货分部退订单货值增减"	,"TYPE_P"),
    AMTYPEQ("Q"	,"订货分部结算货值增减"	,"TYPE_Q"),
    AMTYPES("S"	,"订货分部服务费结转"	,"TYPE_S"),
    AMTYPET("T"	,"担保金额变动"	,"TYPE_T"),
    AMTYPEV("V"	,"移仓商家补偿"	,"TYPE_V"),
    AMTYPEU("U"	,"提货违约金"	,"TYPE_U"),
    AMTYPEW("W"	,"获得提货违约金"	,"TYPE_W"),
    AMTYPEY("Y"	,"预订转单货值增减修正"	,"TYPE_Y"),
    AMTYPEa("a"	,"货值增减服务费修正"	,"TYPE_a"),
    AMTYPEb("b"	,"仓租费"	,"TYPE_b"),
    AMTYPEc("c"	,"融货服务费"	,"TYPE_c"),
    AMTYPEd("d"	,"申购服务费"	,"TYPE_d"),
    AMTYPEe("e"	,"申购冻结款"	,"TYPE_e"),
    AMTYPEf("f"	,"申购冻结款退还"	,"TYPE_f"),
    AMTYPEq("q"	,"发售库存款"	,"TYPE_q"),
    AMTYPEh("h"	,"发售预订单"	,"TYPE_h"),
    AMTYPEi("i"	,"发售退订单"	,"TYPE_i"),
    AMTYPEj("j"	,"发售转让"	,"TYPE_j"),
    AMTYPEk("k"	,"提货服务费退还"	,"TYPE_k"),
    AMTYPEl("l"	,"缴纳推荐费"	,"TYPE_l"),
    AMTYPEm("m"	,"推荐费收益"	,"TYPE_m"),
    AMTYPEn("n"	,"预订违约金"	,"TYPE_n"),
    AMTYPEo("o"	,"预订商品收益"	,"TYPE_o"),
    AMTYPEp("p"	,"区域运营商返佣"	,"TYPE_p"),

    ;

    private  String key;
    private  String remark;

    //数据库对应字段值
    private  String condition;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    private FieldEnum(String key, String remark, String condition){
        this.key = key ;
        this.remark = remark;
        this.condition = condition;
    }
}

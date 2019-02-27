package com.dms.api.common.Enum;

import java.util.Arrays;

/**
 * Created with 陈世灵.
 * Date: 2017/9/13
 * Time: 14:34
 * Description:
 */
public enum ChartType {

    AG_WEIGHT_UP("TOTAL_WEIGHT", "预订银总克重", "PUBLISH:ORDER:%s:AG:WEIGHT:UP", "CACHE:ORDER:%s:AG:WEIGHT:UP", "b", "AG"),
    AG_WEIGHT_DOWN("TOTAL_WEIGHT", "回购银总克重", "PUBLISH:ORDER:%s:AG:WEIGHT:DOWN", "CACHE:ORDER:%s:AG:WEIGHT:DOWN", "s", "AG"),
    AU_WEIGHT_UP("TOTAL_WEIGHT", "预订金总克重", "PUBLISH:ORDER:%s:AU:WEIGHT:UP", "CACHE:ORDER:%s:AU:WEIGHT:UP", "b", "AU"),
    AU_WEIGHT_DOWN("TOTAL_WEIGHT", "回购金总克重", "PUBLISH:ORDER:%s:AU:WEIGHT:DOWN", "CACHE:ORDER:%s:AU:WEIGHT:DOWN", "s", "AU"),


    AG_PREPAY_UP("TOTAL_PRE_PAY", "预订银总预付款", "PUBLISH:ORDER:%s:AG:PREPAY:UP", "CACHE:ORDER:%s:AG:PREPAY:UP", "b", "AG"),
    AG_PREPAY_DOWN("TOTAL_PRE_PAY", "回购银总预付款", "PUBLISH:ORDER:%s:AG:PREPAY:DOWN", "CACHE:ORDER:%s:AG:PREPAY:DOWN", "s", "AG"),
    AU_PREPAY_UP("TOTAL_PRE_PAY", "预订金总预付款", "PUBLISH:ORDER:%s:AU:PREPAY:UP", "CACHE:ORDER:%s:AU:PREPAY:UP", "b", "AU"),
    AU_PREPAY_DOWN("TOTAL_PRE_PAY", "回购金总预付款", "PUBLISH:ORDER:%s:AU:PREPAY:DOWN", "CACHE:ORDER:%s:AU:PREPAY:DOWN", "s", "AU"),

    WEIGHT_TYPE("WEIGHT_TYPE","克重","PUBLISH:ORDER:%s:WEIGHT:TYPE","CACHE:ORDER:%s:WEIGHT:TYPE"),
    PREPAY_TYPE("PREPAY_TYPE","克重","PUBLISH:ORDER:%s:PREPAY:TYPE","CACHE:ORDER:%s:PREPAY:TYPE"),


//    TOTAL_MONEY("TOTAL_MONEY", "商家总资产", "PUBLISH:MONEY:%s:TOTAL", "CACHE:MONEY:%s:TOTAL","",""),
    TOTAL_MONEY("TOTAL_MONEY", "商家总资产", "PUBLISH:MONEY:%s:TOTAL_USER_MONEY", "CACHE:MONEY:%s:TOTAL","",""),
    USER_MONEY("USER_MONEY", "客户资产", "", "CACHE:MONEY:%s:USER","",""),

    /**
     * 商家资产比
     */
    DEALER_ASSET_RATIO("DEALER_ASSET_RATIO", "商家资产比", "PUBLISH:DEALER:%s:ASSET_RATIO", "CACHE:DEALER:%s:ASSET_RATIO", "", ""),

    /**
     * 商家备货金占用率
     */
    RISK_RATE("RISK_RATE", "商家备货金占用率", "PUBLISH:DEALER:%s:RISK_RATE", "CACHE:DEALER:%s:RISK_RATE", "", "");


    //类型名称
    private String type;
    //类型描述
    private String desc;
    //对应订阅key
    private String pKey;
    //对应缓存key
    private String cKey;
    //预订回购标识
    private String bsCode;
    //商品类别标识
    private String material;

    ChartType(String type, String desc, String pKey, String cKey, String bsCode, String material) {
        this.type = type;
        this.desc = desc;
        this.pKey = pKey;
        this.cKey = cKey;
        this.bsCode = bsCode;
        this.material = material;
    }

    ChartType(String type, String desc, String pKey, String cKey) {
        this.type = type;
        this.desc = desc;
        this.pKey = pKey;
        this.cKey = cKey;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getpKey() {
        return pKey;
    }

    public void setpKey(String pKey) {
        this.pKey = pKey;
    }

    public String getcKey() {
        return cKey;
    }

    public void setcKey(String cKey) {
        this.cKey = cKey;
    }

    public String getBsCode() {
        return bsCode;
    }

    public void setBsCode(String bsCode) {
        this.bsCode = bsCode;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public final static ChartType getChartType(String type, String material, String bsCode) {
        ChartType chartType;
        try {
            chartType = Arrays.stream(ChartType.values()).
                    filter(c ->
                            type.equals(c.getType()) && material.equals(c.getMaterial()) && bsCode.equals(c.getBsCode())
                    )
                    .findFirst().get();
        } catch (RuntimeException e) {
            return null;
        }
        return chartType;
    }
    public final static ChartType getChartTypeByName(String name) {
        ChartType chartType;
        try {
            chartType = Arrays.stream(ChartType.values()).
                    filter(c ->
                            name.equals(c.toString())
                    )
                    .findFirst().get();
        } catch (RuntimeException e) {
            return null;
        }
        return chartType;
    }
}

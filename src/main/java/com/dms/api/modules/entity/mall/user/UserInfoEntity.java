package com.dms.api.modules.entity.mall.user;

import com.dms.api.common.Enum.FieldEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class UserInfoEntity implements Serializable{

    private String loginCode;
    @JsonIgnore
    private Integer accStatus;
    private String accStatsRemark;
    private String userName;
    private String phone;
    private String serviceNum;
    private String bmOrgNum;
    private BigDecimal amMarginRemain;
    private Date intoTime;
    @JsonIgnore
    private String levelNum;
    private String levelRemark;

    private final String LEVEL0_5 = "0.5";
    private final String LEVEL0 = "0";
    private final String LEVEL1 = "1";
    private final String LEVEL2 = "2";
    private final String LEVEL3 = "3";
    private final String LEVEL4 = "4";
    private final String LEVEL5 = "5";

    private final String ACCSTATUS1 = "1";
    private final String ACCSTATUS2 = "2";
    private final String ACCSTATUS3 = "3";
    private final String ACCSTATUS4 = "4";
    private final String ACCSTATUS5 = "5";
    private final String ACCSTATUS6 = "6";
    private final String ACCSTATUS7 = "7";

    public String getLoginCode() {
        return loginCode;
    }

    public void setLoginCode(String loginCode) {
        this.loginCode = loginCode;
    }

    public Integer getAccStatus() {
        return accStatus;
    }

    public void setAccStatus(Integer accStatus) {
        this.accStatus = accStatus;
    }

    public String getAccStatsRemark() {
        String resultStr = "--";
        switch (String.valueOf(getAccStatus())){
            case ACCSTATUS1:
                resultStr = FieldEnum.ACCSTATUS1.getRemark();
                break;
            case ACCSTATUS2:
                resultStr = FieldEnum.ACCSTATUS2.getRemark();
                break;
            case ACCSTATUS3:
                resultStr = FieldEnum.ACCSTATUS3.getRemark();
                break;
            case ACCSTATUS4:
                resultStr = FieldEnum.ACCSTATUS4.getRemark();
                break;
            case ACCSTATUS5:
                resultStr = FieldEnum.ACCSTATUS5.getRemark();
                break;
            case ACCSTATUS6:
                resultStr = FieldEnum.ACCSTATUS6.getRemark();
                break;
            case ACCSTATUS7:
                resultStr = FieldEnum.ACCSTATUS7.getRemark();
                break;
            default: resultStr = FieldEnum.LEVEL0_5.getRemark();
        }
        return resultStr;
    }

    public void setAccStatsRemark(String accStatsRemark) {
        this.accStatsRemark = accStatsRemark;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getServiceNum() {
        return serviceNum;
    }

    public void setServiceNum(String serviceNum) {
        this.serviceNum = serviceNum;
    }

    public String getBmOrgNum() {
        return bmOrgNum;
    }

    public void setBmOrgNum(String bmOrgNum) {
        this.bmOrgNum = bmOrgNum;
    }

    public BigDecimal getAmMarginRemain() {
        return amMarginRemain;
    }

    public void setAmMarginRemain(BigDecimal amMarginRemain) {
        this.amMarginRemain = amMarginRemain;
    }

    public Date getIntoTime() {
        return intoTime;
    }

    public void setIntoTime(Date intoTime) {
        this.intoTime = intoTime;
    }

    public String getLevelNum() {
        return levelNum;
    }

    public void setLevelNum(String levelNum) {
        this.levelNum = levelNum;
    }

    public String getLevelRemark() {
        return getLevelNum();
    }

    public void setLevelRemark(String levelRemark) {
        this.levelRemark = levelRemark;
    }
}

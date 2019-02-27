package com.dms.api.modules.entity.basis.org;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 
 */
@Data
public class BmDealerInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;                //主键ID
    private String dealerId;        //商家id
    private String dealerName;      //商家名称
    private String dealerFullName;  //商家全称
    private String dealerCode;      //商家代码
    private String baseInfoIDCard;  //证件号码
    private String baseInfoIDType;  //证件类型
    private String baseInfoName;    //签约名称
    private String baseInfoTelephone;   //电话
    private String dealerType;      //商家类型
    private String remarks;         //备注

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", dealerId=").append(dealerId);
        sb.append(", dealerName=").append(dealerName);
        sb.append(", dealerFullName=").append(dealerFullName);
        sb.append(", dealerCode=").append(dealerCode);
        sb.append(", baseInfoIDCard=").append(baseInfoIDCard);
        sb.append(", baseInfoIDType=").append(baseInfoIDType);
        sb.append(", baseInfoName=").append(baseInfoName);
        sb.append(", baseInfoTelephone=").append(baseInfoTelephone);
        sb.append(", dealerType=").append(dealerType);
        sb.append(", remarks=").append(remarks);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

}
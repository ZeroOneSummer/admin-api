package com.dms.api.modules.entity.basis.org;

import lombok.Data;

@Data
public class BmSysOrganizationWithParent {
    private static final long serialVersionUID = 1L;

    private Long id;                    //主键ID
    private String orgId;               //机构id
    private String orgCode;             //机构代码
    private String orgName;             //机构名称
    private String orgSequence;         //机构注册推荐码
    private String dealerId;            //所属id
    private String brokerDealerId;      //所属经纪商家id
    private String baseInfoIDCard;      //证件号码
    private String baseInfoIDType;      //证件类型
    private String baseInfoTelephone;   //电话
    private String parentId;            //父级机构id
    private String remarks;             //备注
    private BmSysOrganization parent;   //父级机构
    private Integer subOrgCount;        //机构数
    private Integer userCount;          //用户数
    private String levelInfo;           //等级信息

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", orgId=").append(orgId);
        sb.append(", orgCode=").append(orgCode);
        sb.append(", orgName=").append(orgName);
        sb.append(", orgSequence=").append(orgSequence);
        sb.append(", dealerId=").append(dealerId);
        sb.append(", brokerDealerId=").append(brokerDealerId);
        sb.append(", baseInfoIDCard=").append(baseInfoIDCard);
        sb.append(", baseInfoIDType=").append(baseInfoIDType);
        sb.append(", baseInfoTelephone=").append(baseInfoTelephone);
        sb.append(", parentId=").append(parentId);
        sb.append(", remarks=").append(remarks);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

}

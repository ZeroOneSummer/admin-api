package com.dms.api.modules.entity.reserve.coupons;

import lombok.Data;

@Data
public class DealerLoginCode {

    private String loginCode;
    private String userName;
    private String dealerId;
    private String dealerCode;
    private String dealerName;
    private String dealerFullName;

}

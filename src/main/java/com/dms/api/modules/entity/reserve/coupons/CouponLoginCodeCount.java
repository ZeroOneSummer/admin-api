package com.dms.api.modules.entity.reserve.coupons;

import lombok.Data;


@Data
public class CouponLoginCodeCount {

    private String loginCode;
    private Integer count;

}

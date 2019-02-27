package com.dms.api.modules.service.reserve.coupons;


import com.dms.api.modules.entity.reserve.coupons.CouponCheck;
import com.dms.api.modules.entity.reserve.coupons.CouponDetail;

import java.util.List;
import java.util.Map;

/**
 * 券审核
 */
public interface CouponCheckService {

    CouponDetail queryDetail(String couponNo);

    List<CouponCheck> queryList(Map<String, Object> map);

    int queryTotal(Map<String, Object> map);

    void update(CouponCheck couponCheck);

}
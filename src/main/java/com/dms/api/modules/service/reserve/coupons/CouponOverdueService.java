package com.dms.api.modules.service.reserve.coupons;


import com.dms.api.modules.entity.reserve.coupons.CouponOverdue;

import java.util.List;

/**
 * 预付款券失效表
 */
public interface CouponOverdueService {

    void saveBatch(List<CouponOverdue> list);

}
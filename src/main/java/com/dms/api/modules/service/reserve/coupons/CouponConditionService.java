package com.dms.api.modules.service.reserve.coupons;


import com.dms.api.modules.entity.reserve.coupons.CouponCondition;

import java.util.List;
import java.util.Map;

/**
 * 券限制条件
 */
public interface CouponConditionService {

    CouponCondition queryObject(Integer id);

    List<CouponCondition> queryList(Map<String, Object> map);

    int queryTotal(Map<String, Object> map);

    void save(CouponCondition couponCondition);

    void update(CouponCondition couponCondition);

    void delete(Integer id);

    void deleteBatch(Integer[] ids);

    boolean queryRepeat(CouponCondition couponCondition);

}
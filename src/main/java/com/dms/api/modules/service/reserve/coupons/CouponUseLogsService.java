package com.dms.api.modules.service.reserve.coupons;


import com.dms.api.modules.entity.reserve.coupons.CouponUseLogs;

import java.util.List;
import java.util.Map;

/**
 * 预付款券使用记录
 */
public interface CouponUseLogsService {

    CouponUseLogs queryObject(Integer id);

    List<CouponUseLogs> queryList(Map<String, Object> map);

    int queryTotal(Map<String, Object> map);

    void saveBatch(List<CouponUseLogs> list);

    void update(CouponUseLogs couponUseLogs);

    void delete(Integer id);

    void deleteBatch(Integer[] ids);

}
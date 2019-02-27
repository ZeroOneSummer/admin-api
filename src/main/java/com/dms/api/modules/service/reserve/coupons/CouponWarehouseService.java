package com.dms.api.modules.service.reserve.coupons;


import com.dms.api.modules.entity.reserve.coupons.CouponWarehouse;

import java.util.List;
import java.util.Map;

/**
 * 预付款券仓库
 */
public interface CouponWarehouseService {

    CouponWarehouse queryObject(Integer id);

    List<CouponWarehouse> queryList(Map<String, Object> map);

    int queryTotal(Map<String, Object> map);

    void save(CouponWarehouse couponWarehouse, Integer createNum, String useUser);

    void update(CouponWarehouse couponWarehouse);

    void updateBatch(Integer[] ids);

    void delete(Integer id);

    void deleteBatch(Integer[] ids);

}
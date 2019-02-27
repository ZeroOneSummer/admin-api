package com.dms.api.modules.dao.reserve.coupons;


import com.dms.api.modules.base.BaseDao;
import com.dms.api.modules.entity.reserve.coupons.CouponWarehouse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CouponWarehouseDao extends BaseDao<CouponWarehouse> {

    void saveList(List<CouponWarehouse> list);

    void updateBatch(Integer[] ids);

}
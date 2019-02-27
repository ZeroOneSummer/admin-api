package com.dms.api.modules.dao.reserve.coupons;


import com.dms.api.modules.base.BaseDao;
import com.dms.api.modules.entity.reserve.coupons.CouponOverdue;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CouponOverdueDao extends BaseDao<CouponOverdue> {
}
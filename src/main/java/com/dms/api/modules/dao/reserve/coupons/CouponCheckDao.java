package com.dms.api.modules.dao.reserve.coupons;


import com.dms.api.modules.base.BaseDao;
import com.dms.api.modules.entity.reserve.coupons.CouponCheck;
import com.dms.api.modules.entity.reserve.coupons.CouponDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface CouponCheckDao extends BaseDao<CouponCheck> {

    CouponDetail queryDetail(@Param("couponNo") String couponNo);

    Map<String, Object> queryAccId(@Param("couponNo") String couponNo);

    void addCouponVerifyRecord(CouponCheck couponCheck);

}
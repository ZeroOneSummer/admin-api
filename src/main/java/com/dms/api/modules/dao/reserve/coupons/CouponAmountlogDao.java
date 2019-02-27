package com.dms.api.modules.dao.reserve.coupons;


import com.dms.api.modules.base.BaseDao;
import com.dms.api.modules.entity.reserve.coupons.CouponAmountlog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface CouponAmountlogDao extends BaseDao<CouponAmountlog> {


    void insertData(Map<String, Object> params);

    //int queryCouponSnNumber(@Param("couponNo") String couponNo );

    int queryCouponsExpired(@Param("couponNo") String couponNo);

    int queryUseCoupons(@Param("couponNo") String couponNo);

    int queryBalanceCoupons(@Param("couponNo") String couponNo);

    int queryPocketCoupons(@Param("couponNo") String couponNo);

    //获取券批次的结余全部序列号
    //List<Map<String, String>> queryBalanceCouponSn(@Param("couponNo") String couponNo);

}
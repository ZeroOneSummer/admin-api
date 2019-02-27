package com.dms.api.modules.dao.reserve.coupons;


import com.dms.api.modules.base.BaseDao;
import com.dms.api.modules.entity.reserve.coupons.CouponCondition;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface CouponConditionDao extends BaseDao<CouponCondition> {

    int queryRepeat(CouponCondition couponCondition);

    int queryCount(Map<String, Object> map);

}
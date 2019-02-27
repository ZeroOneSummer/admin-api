package com.dms.api.modules.dao.reserve.coupons;

import com.dms.api.modules.base.BaseDao;
import com.dms.api.modules.entity.reserve.coupons.CouponUseLogs;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CouponUseLogsDao extends BaseDao<CouponUseLogs> {



    int insertBatchCouponUseLogs(List<CouponUseLogs> list);

}
package com.dms.api.modules.service.reserve.coupons;

import com.dms.api.common.utils.R;
import com.dms.api.modules.entity.reserve.coupons.CouponDetail;
import com.dms.api.modules.entity.sys.user.SysUserEntity;

import java.util.List;
import java.util.Map;

/**
 * 预付款券基础信息
 */
public interface CouponDetailService {

    SysUserEntity getUser();

    CouponDetail queryObject(Integer id);

    List<CouponDetail> queryList(Map<String, Object> map);

    int queryTotal(Map<String, Object> map);

    R save(CouponDetail couponDetail);

    R update(CouponDetail couponDetail);

    void delete(Integer id);

    R deleteBatch(Integer[] ids);

    String getWarehouseIdsByIds(Integer[] ids);

    String getConditionIdsByIds(Integer[] ids);

    Map<String, Object> getLoginCodeByDealerId(String dealerId);


    /**
     * 手动发券
     * @param dealerId
     * @param loginCodes
     */
    R grantCoupon(String dealerId, String couponNo, String loginCodes);

    /**
     * 添加待审核记录，并更新预待审核券状态
     * @param couponDetail
     */
    void addCouponVerifyRecord(CouponDetail couponDetail);

}
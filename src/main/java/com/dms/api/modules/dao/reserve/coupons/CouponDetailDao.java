package com.dms.api.modules.dao.reserve.coupons;

import com.dms.api.modules.base.BaseDao;
import com.dms.api.modules.entity.reserve.coupons.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface CouponDetailDao extends BaseDao<CouponDetail> {

    String getWarehouseIdsByIds(Integer[] ids);

    String getConditionIdsByIds(Integer[] ids);

    Map<String, Object> getLoginCodeByDealerId(String dealerId);

    List<DealerLoginCode> queryDealerLoginCode(
            @Param("dealerId") String dealerId,
            @Param("loginCodes") String[] loginCodes
    );

    List<CouponDetail> userSureReceiveList(
           @Param("dealerId") String dealerId,
           @Param("couponNo") String couponNo
    );

    /**
     * 用户领券方法
     * @param loginCode
     * @param couponSn
     * @return
     */
    int updateReceiveCoupon(
            @Param("loginCode") String loginCode,
            @Param("couponSn") String couponSn,
            @Param("couponUseEndDate") String couponUseEndDate);


    List<CouponLoginCodeCount> queryCouponLoginCodeCount(@Param("couponNo") String couponNo);

    String couponIsAudit(@Param("couponNo") String couponNo);

    int updateStatus(CouponDetail couponDetail);
    int updateVerifyStatus(CouponDetail couponDetail);


    List<CouponWarehouse> queryUserCoupon(@Param("loginCode") String loginCode);

    //根据推荐码获取服务商编号
    String queryDealerCodeBySequence(@Param("orgSequence") String orgSequence);

    /**
     *
     * 将券 改为过期
     * @param list
     * @return
     */
    int updateCouponWarehouseStatus(
            @Param("useStatus") String useStatus,
            @Param("couponSnList") List<String> list
    );

    int insertBatchCouponUseLogs(List<CouponUseLogs> list);


    /**
     * 批量新增 券过期数据
     * @param list
     * @return
     */
    int insertBatchCouponOverdue(List<CouponOverdue> list);

    /**
     * 根据券的批次号查询该券的信息
     */
    List<CouponDetail> queryCouponNo(@Param("couponNo") String couponNo);


}
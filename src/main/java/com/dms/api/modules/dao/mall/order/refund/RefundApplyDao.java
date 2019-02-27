package com.dms.api.modules.dao.mall.order.refund;

import com.dms.api.common.utils.Query;
import com.dms.api.modules.base.BaseDao;
import com.dms.api.modules.entity.mall.order.refund.RefundApplyAndVerifyRecordVo;
import com.dms.api.modules.entity.mall.order.refund.RefundApplyEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 退差价申请表
 * @author
 * @email @danpacmall.com
 * @date 2017-12-08 16:25:13
 */
@Mapper
public interface RefundApplyDao extends BaseDao<RefundApplyEntity> {

    List<RefundApplyAndVerifyRecordVo> refundApplyAndVerifyRecordVoList(Query query);

    RefundApplyAndVerifyRecordVo info(@Param("id") Long id, @Param("type") long type);
}

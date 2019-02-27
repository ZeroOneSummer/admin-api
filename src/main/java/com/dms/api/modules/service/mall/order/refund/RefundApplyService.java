package com.dms.api.modules.service.mall.order.refund;

import com.dms.api.common.utils.Query;
import com.dms.api.common.utils.R;
import com.dms.api.modules.entity.mall.order.refund.RefundApplyAndVerifyRecordVo;
import com.dms.api.modules.entity.mall.order.refund.RefundApplyEntity;

import java.util.List;
import java.util.Map;

/**
 * 退差价申请表
 * 
 * @author
 * @email @danpacmall.com
 * @date 2017-12-08 16:25:13
 */
public interface RefundApplyService {
	
	RefundApplyEntity queryObject(Long id);
	
	List<RefundApplyEntity> queryList(Map <String, Object> map);

	int queryTotal(Map <String, Object> map);
	
	void save(RefundApplyEntity refundApply);
	
	void update(RefundApplyEntity refundApply);
	
	void delete(Long id);
	
	void deleteBatch(Long[] ids);

	/**
	 * 获取退差价订单信息
	 * @param query
	 * @return
	 */
    List<RefundApplyAndVerifyRecordVo> refundApplyAndVerifyRecordVoList(Query query);

    RefundApplyAndVerifyRecordVo info(Long id, long type);

	/**
	 * 订单提交审核
	 * @param refundApplyAndVerifyRecordVo
	 * @param userId
	 * @return
	 */
	R commitApply(RefundApplyAndVerifyRecordVo refundApplyAndVerifyRecordVo, Long userId);
}

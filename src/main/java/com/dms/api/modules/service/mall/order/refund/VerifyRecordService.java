package com.dms.api.modules.service.mall.order.refund;

import com.dms.api.modules.entity.mall.order.refund.VerifyRecordEntity;

import java.util.List;
import java.util.Map;

/**
 * 审核表
 * 
 * @author
 * @email @danpacmall.com
 * @date 2017-12-08 16:25:13
 */
public interface VerifyRecordService {
	
	VerifyRecordEntity queryObject(Long id);
	
	List<VerifyRecordEntity> queryList(Map <String, Object> map);

	int queryTotal(Map <String, Object> map);
	
	void save(VerifyRecordEntity verifyRecord);
	
	void update(VerifyRecordEntity verifyRecord);
	
	void delete(Long id);
	
	void deleteBatch(Long[] ids);
}

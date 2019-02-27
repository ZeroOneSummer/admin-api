package com.dms.api.modules.service.report.bean;

import com.dms.api.modules.entity.report.bean.BountyDailySettlementEntity;

import java.util.List;
import java.util.Map;

/**
 * 服务商/机构K豆日结表
 * @author 40
 * @email chenshiling@danpacmall.com
 * @date 2018-01-16 10:55:44
 */
public interface BountyDailySettlementService {
	
	BountyDailySettlementEntity queryObject(Long id);

//	List<BountyDailySettlementEntity> queryList(Map<String, Object> map);

	List<BountyDailySettlementEntity> queryListByDealer(Map <String, Object> map);

	List<BountyDailySettlementEntity> queryListByOrg(Map <String, Object> map);

	int queryTotal(Map <String, Object> map);
	
	void save(BountyDailySettlementEntity bountyDailySettlement);
	
	void update(BountyDailySettlementEntity bountyDailySettlement);
	
	void delete(Long id);
	
	void deleteBatch(Long[] ids);
}

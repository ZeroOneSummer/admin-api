package com.dms.api.modules.dao.report.bean;

import com.dms.api.common.utils.Query;
import com.dms.api.modules.base.BaseDao;
import com.dms.api.modules.entity.report.bean.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 奖励金_资金流水表
 * @email @danpacmall.com
 * @date 2017-11-07 09:57:36
 */
@Mapper
public interface HxBountyDao extends BaseDao<HxBountyEntity> {

	// K豆总额
	BigDecimal totalBounty(Query query);
	
	//可用K豆总额
	BigDecimal usableBounty(Query query);
	
	//已消费K豆总额
	BigDecimal consumeBounty(Query query);
	
	//已过期K豆总额
	BigDecimal pastBounty(Query query);

	List<SingleConsumeDetail> ConsumeDetail(Query query);

	int dealerTotal(Query query);

	int singleTotal(Query query);

	int ConsumeDetailTotal(Query query);
	
	//机构数目
	int orgTotalFirst(Query query);
	
	//获取机构列表
	List<OrgCount> orgList(Query query);
	
	//服务商数目
	int dealerTotalFirst(Query query);
		
	//服务商列表
	List<DealerCount> dealerListFirst(Query query);

	//根据服务商获得b_bill表数据
	List<BBillEntity> billsByDealers(Query query);

	//根据服务商获得b_invalidamountflow表数据
	List<BInvalidamountfolwEntity> invalidByDealers(Query query);

	List<DealerCount> countCurrentByDealerId(Query query);

	List<DealerCount> countCurrentInvalidByDealerId(Query query);

	//统计日结报表汇总历史数据
	List<BountyDailySettlementEntity> countHistory(Query query);

	//根据机构获得b_bill表数据
	List<BBillEntity> billsByOrgs(Query query);

	//根据机构获得b_invalidamountflow表数据
	List<BInvalidamountfolwEntity> invalidByOrgs(Query query);

	List<OrgCount> countCurrentByOrgId(Query query);

	List<OrgCount> countCurrentInvalidByOrgId(Query query);

	//获取用户相关信息
	List<SingleCount> singleInfo(Query query);

	List<SingleCount> countSingleBounty(Query query);

	//获取用户相关K豆信息
	List<BBillEntity> billsBySingle(Query query);

	List<BInvalidamountfolwEntity> invalidBySingle(Query query);

	BigDecimal valalidBounty(Query query);

	//根据broker_dealer_id找机构
	List<Long> getOrgsByDealerId(@Param("dealerId") Long dealerId);

	String getMinDateByOrgId(Map map);

	List<TypeSettlementEntity> getDailyInvalidByOrgId(Map map);

	List<TypeSettlementEntity> getSettlementByOrgId(Map map);

	String getMinDateByDealerId(Map map);

	List<TypeSettlementEntity> getSettlementByDealerId(Map map);

	List<TypeSettlementEntity> getDailyInvalidByDealerId(Map map);

	List<TypeSettlementEntity> getTotalByDealerId(Map map);
}

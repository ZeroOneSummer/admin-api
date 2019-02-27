package com.dms.api.modules.service.report.bean;

import com.dms.api.common.utils.Query;
import com.dms.api.modules.entity.report.bean.*;
import com.dms.api.modules.entity.sys.user.SysUserEntity;

import java.util.List;
import java.util.Map;

/**
 * 奖励金_资金流水表
 */
public interface HxBountyService {

	//统计K豆总表
	Map<String, Object> countBounty(SysUserEntity user);

	List<DealerCount> countDealerBounty(Query query);

	//机构K豆统计
	List<OrgCount> countOrgBounty(Query query);

	//个人K豆统计
	List<SingleCount> countSingleBounty(Query query);

	//获取消费详情
	List<SingleConsumeDetail> ConsumeDetail(Query query);

	int dealerTotal(Query query);

	int singleTotal(SysUserEntity user, Query query);

	int ConsumeDetailTotal(Query query);

	int getOrgsSizeByUser(SysUserEntity user, Query query);
}

package com.dms.api.modules.dao.report.bean;

import com.dms.api.modules.base.BaseDao;
import com.dms.api.modules.entity.report.bean.BountyDailySettlementEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 服务商/机构K豆日结表
 * @author 40
 * @email chenshiling@danpacmall.com
 * @date 2018-01-16 10:55:44
 */
@Mapper
public interface BountyDailySettlementDao extends BaseDao<BountyDailySettlementEntity> {

	void insertBatch(List <BountyDailySettlementEntity> list);
}

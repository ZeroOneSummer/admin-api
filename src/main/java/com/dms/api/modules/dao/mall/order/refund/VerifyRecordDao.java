package com.dms.api.modules.dao.mall.order.refund;

import com.dms.api.modules.base.BaseDao;
import com.dms.api.modules.entity.mall.order.refund.VerifyRecordEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 审核表
 * 
 * @author
 * @email @danpacmall.com
 * @date 2017-12-08 16:25:13
 */
@Mapper
public interface VerifyRecordDao extends BaseDao<VerifyRecordEntity> {

    int updateByReferId(VerifyRecordEntity record);

    int insertBatch(List <VerifyRecordEntity> list);

    int verifyBatch(Map map);
}

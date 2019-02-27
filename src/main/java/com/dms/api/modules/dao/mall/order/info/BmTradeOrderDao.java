package com.dms.api.modules.dao.mall.order.info;

import com.dms.api.modules.entity.mall.order.info.BmTradeOrder;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BmTradeOrderDao {

    BmTradeOrder selectByPrimaryKey(Long id);

    List<BmTradeOrder> selectByDate(Map <String, Object> record);

    List<BmTradeOrder> selectByDate_chart(Map <String, Object> record);
}
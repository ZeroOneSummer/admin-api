package com.dms.api.modules.dao.mall.order.info;

import com.dms.api.modules.entity.mall.order.info.BmTradeHoldBill;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BmTradeHoldBillDao {

    BmTradeHoldBill selectByPrimaryKey(Long id);

    BmTradeHoldBill queryObject(Long id);

    List<BmTradeHoldBill> selectByDate(Map <String, Object> record);

    List<BmTradeHoldBill> queryList(Map <String, Object> record);

    List<BmTradeHoldBill> querySimpleList(Map <String, Object> record);

    int queryTotal(Map <String, Object> record);
}
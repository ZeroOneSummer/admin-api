package com.dms.api.modules.service.mall.order.info;

import com.dms.api.common.utils.R;
import com.dms.api.modules.entity.mall.order.info.BmTradeHoldBill;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * BmTradeHoldBillService
 */
public interface BmTradeHoldBillService {

    List<BmTradeHoldBill> queryList(Map <String, Object> map);

    Integer queryTotal(Map <String, Object> map);

    BmTradeHoldBill queryObject(Long id);

    HashMap<String, Object> historyData(Map record);

    R historyDistribution(Map record);

}

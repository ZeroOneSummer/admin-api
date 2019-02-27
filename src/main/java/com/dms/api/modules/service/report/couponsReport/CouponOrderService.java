package com.dms.api.modules.service.report.couponsReport;


import com.dms.api.modules.entity.report.couponsReport.CouponOrder;

import java.util.List;
import java.util.Map;

public interface CouponOrderService {

    List<CouponOrder> queryList(Map<String, Object> map);
}

package com.dms.api.modules.service.report.couponsReport;


import com.dms.api.modules.entity.report.couponsReport.CouponAmlog;

import java.util.List;
import java.util.Map;

public interface CouponAmlogService {

    List<CouponAmlog> queryList(Map<String, Object> map);

    int queryTotal(Map<String, Object> map);
}

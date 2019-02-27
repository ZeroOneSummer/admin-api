package com.dms.api.modules.service.report.couponsReport;


import com.dms.api.modules.entity.report.couponsReport.CouponCompareAccount;

import java.util.List;
import java.util.Map;

public interface CouponCompareAccountService {

    List<CouponCompareAccount> queryList(Map<String, Object> map);

    int queryTotal(Map<String, Object> map);
}

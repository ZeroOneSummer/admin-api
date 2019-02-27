package com.dms.api.modules.service.report.couponsReport.impl;


import com.dms.api.modules.dao.report.couponsReport.CouponCompareAccountDao;
import com.dms.api.modules.entity.report.couponsReport.CouponCompareAccount;
import com.dms.api.modules.service.report.couponsReport.CouponCompareAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CouponCompareAccountServiceImpl implements CouponCompareAccountService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    CouponCompareAccountDao couponCompareAccountDao;

    @Override
    public List <CouponCompareAccount> queryList(Map <String, Object> record) {
        return couponCompareAccountDao.queryList(record);
    }

    @Override
    public int queryTotal(Map <String, Object> record) {
        return couponCompareAccountDao.queryTotal(record);
    }
}

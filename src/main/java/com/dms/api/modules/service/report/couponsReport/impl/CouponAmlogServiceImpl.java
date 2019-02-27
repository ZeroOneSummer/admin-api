package com.dms.api.modules.service.report.couponsReport.impl;


import com.dms.api.modules.dao.report.couponsReport.CouponAmlogDao;
import com.dms.api.modules.entity.report.couponsReport.CouponAmlog;
import com.dms.api.modules.service.report.couponsReport.CouponAmlogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CouponAmlogServiceImpl implements CouponAmlogService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    CouponAmlogDao couponAmlogDao;

    @Override
    public List <CouponAmlog> queryList(Map <String, Object> record) {
        return couponAmlogDao.queryList(record);
    }

    @Override
    public int queryTotal(Map <String, Object> record) {
        return couponAmlogDao.queryTotal(record);
    }
}

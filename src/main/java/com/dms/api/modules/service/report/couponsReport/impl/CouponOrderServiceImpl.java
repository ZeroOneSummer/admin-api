package com.dms.api.modules.service.report.couponsReport.impl;


import com.dms.api.modules.dao.report.couponsReport.CouponOrderDao;
import com.dms.api.modules.entity.report.couponsReport.CouponOrder;
import com.dms.api.modules.service.report.couponsReport.CouponOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CouponOrderServiceImpl implements CouponOrderService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    CouponOrderDao couponOrderDao;

    @Override
    public List <CouponOrder> queryList(Map <String, Object> map) {
        return couponOrderDao.queryList(map);
    }

}

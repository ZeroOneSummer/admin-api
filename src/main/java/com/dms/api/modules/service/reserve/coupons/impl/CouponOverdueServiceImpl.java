package com.dms.api.modules.service.reserve.coupons.impl;


import com.dms.api.modules.dao.reserve.coupons.CouponOverdueDao;
import com.dms.api.modules.entity.reserve.coupons.CouponOverdue;
import com.dms.api.modules.service.reserve.coupons.CouponOverdueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CouponOverdueServiceImpl implements CouponOverdueService {

    @Autowired
    private CouponOverdueDao couponOverdueDao;

    @Override
    public void saveBatch(List<CouponOverdue> list) {
        couponOverdueDao.saveBatch(list);
    }
}
package com.dms.api.modules.service.reserve.coupons.impl;


import com.dms.api.modules.dao.reserve.coupons.CouponAmountlogDao;
import com.dms.api.modules.entity.reserve.coupons.CouponAmountlog;
import com.dms.api.modules.service.reserve.coupons.CouponAmountlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CouponAmountlogServiceImpl implements CouponAmountlogService {

    @Autowired
    private CouponAmountlogDao couponAmountlogDao;

    @Override
    public void save(CouponAmountlog couponAmountlog) {
        couponAmountlogDao.save(couponAmountlog);
    }

}
package com.dms.api.modules.service.reserve.coupons.impl;


import com.dms.api.modules.dao.reserve.coupons.CouponConditionDao;
import com.dms.api.modules.entity.reserve.coupons.CouponCondition;
import com.dms.api.modules.service.reserve.coupons.CouponConditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CouponConditionServiceImpl implements CouponConditionService {

    @Autowired
    private CouponConditionDao couponConditionDao;

    @Override
    public CouponCondition queryObject(Integer id) {
        return couponConditionDao.queryObject(id);
    }

    @Override
    public List<CouponCondition> queryList(Map<String, Object> map) {
        return couponConditionDao.queryList(map);
    }

    @Override
    public int queryTotal(Map <String, Object> map) {
        return couponConditionDao.queryTotal(map);
    }

    @Override
    public void save(CouponCondition couponCondition) {
        couponConditionDao.save(couponCondition);
    }

    @Override
    public void update(CouponCondition couponCondition) {
        couponConditionDao.update(couponCondition);
    }

    @Override
    public void delete(Integer id) {
        couponConditionDao.delete(id);
    }

    @Override
    public void deleteBatch(Integer[] ids) {
        couponConditionDao.deleteBatch(ids);
    }

    @Override
    public boolean queryRepeat(CouponCondition couponCondition) {
        return couponConditionDao.queryRepeat(couponCondition) > 0 ? true : false ;
    }
}
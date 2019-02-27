package com.dms.api.modules.service.reserve.coupons.impl;


import com.dms.api.modules.dao.reserve.coupons.CouponUseLogsDao;
import com.dms.api.modules.entity.reserve.coupons.CouponUseLogs;
import com.dms.api.modules.service.reserve.coupons.CouponUseLogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CouponUseLogsServiceImpl implements CouponUseLogsService {

    @Autowired
    private CouponUseLogsDao couponUseLogsDao;

    @Override
    public CouponUseLogs queryObject(Integer id) {
        return couponUseLogsDao.queryObject(id);
    }

    @Override
    public List<CouponUseLogs> queryList(Map<String, Object> map) {
        return couponUseLogsDao.queryList(map);
    }

    @Override
    public int queryTotal(Map <String, Object> map) {
        return couponUseLogsDao.queryTotal(map);
    }

    @Override
    public void saveBatch(List <CouponUseLogs> list) {
        couponUseLogsDao.saveBatch(list);
    }

    @Override
    public void update(CouponUseLogs couponUseLogs) {
        couponUseLogsDao.update(couponUseLogs);
    }

    @Override
    public void delete(Integer id) {
        couponUseLogsDao.delete(id);
    }

    @Override
    public void deleteBatch(Integer[] ids) {
        couponUseLogsDao.deleteBatch(ids);
    }

}
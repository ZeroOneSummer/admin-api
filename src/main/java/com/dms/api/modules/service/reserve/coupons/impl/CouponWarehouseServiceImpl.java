package com.dms.api.modules.service.reserve.coupons.impl;


import com.dms.api.common.utils.StringUtils;
import com.dms.api.modules.dao.reserve.coupons.CouponWarehouseDao;
import com.dms.api.modules.entity.reserve.coupons.CouponUseLogs;
import com.dms.api.modules.entity.reserve.coupons.CouponWarehouse;
import com.dms.api.modules.service.reserve.coupons.CouponUseLogsService;
import com.dms.api.modules.service.reserve.coupons.CouponWarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class CouponWarehouseServiceImpl implements CouponWarehouseService {

    @Autowired
    private CouponWarehouseDao couponWarehouseDao;

    @Autowired
    private CouponUseLogsService couponUseLogsService;

    @Override
    public CouponWarehouse queryObject(Integer id) {
        return couponWarehouseDao.queryObject(id);
    }

    @Override
    public List<CouponWarehouse> queryList(Map<String, Object> map) {
        return couponWarehouseDao.queryList(map);
    }

    @Override
    public int queryTotal(Map <String, Object> map) {
        return couponWarehouseDao.queryTotal(map);
    }

    @Override
    public void save(CouponWarehouse couponWarehouse,Integer createNum,String useUser) {

        List<CouponWarehouse> list = new ArrayList <>();
        List<CouponUseLogs> list2 = new ArrayList <>();
        String couponSn = couponWarehouse.getCouponSn(); //与couponNo一样
        //生成券
        CouponWarehouse couponWarehouse2 = null;
        for(int i = 1; i <= createNum; i++){
            couponWarehouse2 = new CouponWarehouse();

            couponWarehouse2.setCouponSn(couponWarehouse.getCouponSn());
            couponWarehouse2.setCouponNo(couponWarehouse.getCouponNo());
            couponWarehouse2.setPrice(couponWarehouse.getPrice());
            couponWarehouse2.setCouponPass(couponWarehouse.getCouponPass());
            couponWarehouse2.setLogincode(couponWarehouse.getLogincode());
            couponWarehouse2.setUseStatus(couponWarehouse.getUseStatus());
            couponWarehouse2.setUseTime(couponWarehouse.getUseTime());

            couponWarehouse2.setCouponSn(couponSn + "-" + StringUtils.leftPad(i+"",3,"0"));
            list.add(couponWarehouse2);
        }
        list.stream().forEach(warehouse -> {
            CouponUseLogs logs = new CouponUseLogs();
            logs.setCouponSn(warehouse.getCouponSn());
            logs.setUseType("0"); //初始化
            logs.setUseTime(new Date());
            logs.setUseUser(useUser);
            list2.add(logs);
        });

        if(!list.isEmpty()){
            couponWarehouseDao.saveList(list);
        }
        if(!list2.isEmpty()){
            //日志
            couponUseLogsService.saveBatch(list2);
        }
    }

    @Override
    public void update(CouponWarehouse couponWarehouse) {
        couponWarehouseDao.update(couponWarehouse);
    }

    @Override
    public void updateBatch(Integer[] ids) {
        couponWarehouseDao.updateBatch(ids);
    }

    @Override
    public void delete(Integer id) {
        couponWarehouseDao.delete(id);
    }

    @Override
    public void deleteBatch(Integer[] ids) {
        couponWarehouseDao.deleteBatch(ids);
    }

}
package com.dms.api.modules.controller.reserve.coupons;


import com.dms.api.common.utils.PageUtils;
import com.dms.api.common.utils.Query;
import com.dms.api.common.utils.R;
import com.dms.api.common.utils.StringUtils;
import com.dms.api.modules.base.AbstractController;
import com.dms.api.modules.entity.reserve.coupons.CouponWarehouse;
import com.dms.api.modules.service.reserve.coupons.CouponWarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("couponwarehouse")
public class CouponWarehouseController extends AbstractController {

    @Autowired
    CouponWarehouseService couponWarehouseService;

    @RequestMapping("list")
    public R list(@RequestParam Map<String, Object> params) {

        //useStatus改用传数组的方式
        if (params.get("useStatus") != null) {
            String useStatus = (String) params.get("useStatus");
            if(!"".equals(useStatus.trim())) {
                if(StringUtils.equals(useStatus.trim(),"-2")){//如果查询已过期，则同时查 -2、-3
                    params.put("useStatus", new String[]{"-2","-3"});
                }else{
                    params.put("useStatus", new String[]{useStatus});
                }
            }
        }

        Query query = new Query(params);
        PageUtils result = null;
        List<CouponWarehouse> couponWarehouses = couponWarehouseService.queryList(query);
        int total = couponWarehouseService.queryTotal(query);
        result = new PageUtils(couponWarehouses,total,query);

        return R.ok().put("page", result);
    }

}

package com.dms.api.modules.controller.reserve.coupons;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.dms.api.common.utils.*;
import com.dms.api.modules.base.AbstractController;
import com.dms.api.modules.entity.reserve.coupons.CouponCheck;
import com.dms.api.modules.entity.reserve.coupons.CouponDetail;
import com.dms.api.modules.entity.sys.user.SysUserEntity;
import com.dms.api.modules.service.reserve.coupons.CouponCheckService;
import com.dms.api.modules.service.reserve.coupons.CouponDetailService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("couponCheck")
public class CouponCheckController extends AbstractController {

    @Autowired
    CouponCheckService couponCheckService;

    @Autowired
    CouponDetailService couponDetailService;

    @RequestMapping("/list")
    @RequiresPermissions("couponCheck:list")
    public R list(@RequestParam Map<String, Object> params) {

        Query query = new Query(params);
        //查看权限
        SysUserEntity user = getUser();
        if(!user.isSuperAdmin() && !user.isSysSuperAdmin()){
           Long dealerId = user.getDealerId() == null ? null : user.getDealerId();
           if (dealerId == null){
               return R.error("该账户不是服务商账号");
           }else{
               query.put("dealerId", dealerId);
           }
        }

        PageUtils result = null;
        List<CouponCheck> couponChecks = couponCheckService.queryList(query);
        int total = couponCheckService.queryTotal(query);
        result = new PageUtils(couponChecks,total,query);

        return R.ok().put("page", result);
    }


    @RequestMapping("/info/{no}")
    @RequiresPermissions("couponCheck:info")
    public R info(@PathVariable("no") String no){
        CouponDetail couponDetail = couponCheckService.queryDetail(no);
        return R.ok().put("couponDetail", couponDetail);
    }

    @RequestMapping("/update")
    @RequiresPermissions("couponCheck:update")
    public R update(@RequestBody CouponCheck couponCheck) {
        //参数验证
        String couponNo = couponCheck.getCouponNo() == null ? null : couponCheck.getCouponNo();
        String id = couponCheck.getId() == null ? null : couponCheck.getId().toString();
        String loginPwd = couponCheck.getLoginPwd() == null ? null : couponCheck.getLoginPwd();
        String payPwd = couponCheck.getPayPwd() == null ? null : couponCheck.getPayPwd();
        String status = couponCheck.getStatus() == null ? null : couponCheck.getStatus();
        String endDate = couponCheck.getEndDate() == null ? null : couponCheck.getEndDate();
        if ("1".equals(status)){ //审核为通过才验证
            if (StringUtils.isBlank(couponNo)
                    || StringUtils.isBlank(id)
                    || StringUtils.isBlank(loginPwd)
                    || StringUtils.isBlank(payPwd)
                    || StringUtils.isBlank(status)
                    || StringUtils.isBlank(endDate)
            ){


                return R.error("审核失败,参数为不完整");
            }



            // 审核过程中  审核时间超过 券截止时间
            Date start=DateUtil.parse(DateUtil.now(),"yyyy-MM-dd");
            Date end=DateUtil.parse(endDate,"yyyy-MM-dd");
            long betweenDay = DateUtil.between(start, end, DateUnit.DAY,false);
            if(betweenDay<0){
                return R.error("审核失败,截止日期小于当前日期");
            }


        }

        SysUserEntity user = getUser();
        couponCheck.setUpdateTime(DateUtil.date());
        couponCheck.setUpdateUser(user.getUsername());

        try {
            couponCheckService.update(couponCheck);
        } catch (CouponException e) {
            e.printStackTrace();
            logger.error("优惠券划账异常，异常信息：{}", e);
            return R.error(e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            logger.error("优惠券获取异常，异常信息：{}", e);
            return R.error("审核失败，券生成异常");
        }

        return R.ok();
    }

}

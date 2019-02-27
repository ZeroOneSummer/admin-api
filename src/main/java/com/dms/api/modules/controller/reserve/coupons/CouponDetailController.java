package com.dms.api.modules.controller.reserve.coupons;

import com.alibaba.fastjson.JSONObject;
import com.dms.api.common.utils.*;
import com.dms.api.modules.dao.reserve.coupons.CouponConditionDao;
import com.dms.api.modules.entity.reserve.coupons.CouponDetail;
import com.dms.api.modules.entity.sys.user.SysUserEntity;
import com.dms.api.modules.service.reserve.coupons.CouponDetailService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.*;

/**
 * @author jp
 * @Description: 商城优惠券
 * @date 2018/6/26 16:22
 */
@RequestMapping("coupon")
@RestController
public class CouponDetailController {
    private static Logger logger = LoggerFactory.getLogger(CouponDetailController.class);

    @Autowired
    private CouponDetailService couponDetailService; //优惠券基础信息

    @Autowired
    private CouponConditionDao couponConditionDao; //优惠券条件

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("coupon:detail:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        //获取用户权限
        SysUserEntity user = couponDetailService.getUser();
        if (!user.isSysSuperAdmin() && !user.isSuperAdmin()){
            Long dealerId = user.getDealerId() != null ? user.getDealerId() : null;
            if (dealerId == null) return R.error("查询失败，服务商不存在");
            query.put("dealerId", dealerId.toString());
        }

        List<CouponDetail> couponDetailList = couponDetailService.queryList(query);
        int total = couponDetailService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(couponDetailList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("coupon:detail:info")
    public R info(@PathVariable("id") Integer id){
        CouponDetail couponDetail = couponDetailService.queryObject(id);
        return R.ok().put("couponDetail", couponDetail);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("coupon:detail:save")
    public R save(@RequestBody CouponDetail couponDetail){

        try {
            if(couponDetail.getCouponNumber() > 500){
                return R.error("券初始化数量不能超过500");
            }

            return couponDetailService.save(couponDetail);
        }catch (Exception e) {
            e.printStackTrace();
            logger.error("优惠券获取异常，异常信息：{}", e);
            return R.error("券生成异常");
        }

    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("coupon:detail:update")
    public R update(@RequestBody CouponDetail couponDetail){
        //时间格式化
        if ("".equals(couponDetail.getStartDate())){
            couponDetail.setStartDate(null);
        }
        if ("".equals(couponDetail.getEndDate())){
            couponDetail.setEndDate(null);
        }
        //获取用户信息
        SysUserEntity user = couponDetailService.getUser();
        couponDetail.setUpdateUser(user.getUserId().toString()); //更新者
        couponDetail.setUpdateTime(new Date());

        try {
            return couponDetailService.update(couponDetail);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("更新异常，异常信息：{}", e);
            return R.error("更新失败");
        }

    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("coupon:detail:delete")
    public R delete(@RequestBody Integer[] ids){
        //在service层处理，添加事务控制
        try {
            return couponDetailService.deleteBatch(ids);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("删除异常，异常信息：{}", e);
            return R.error("删除失败");
        }

    }

    /**
     * 手动发券
     */
    @RequestMapping("/grantCoupon")
    public R grantCoupon(@RequestBody Map<String, Object> params){

        //参数校验
        String dealerId = params.get("dealerId") == null ? null : params.get("dealerId").toString();
        String couponNo = params.get("couponNo") == null ? null : (String) params.get("couponNo");
        String loginCodes = params.get("loginCodes") == null ? null : (String) params.get("loginCodes");
        if (StringUtils.isBlank(dealerId) || StringUtils.isBlank(loginCodes)) {
            return R.error("dealerId或loginCodes为空");
        }

        //在service层处理，添加事务控制
        try {
            return couponDetailService.grantCoupon(dealerId,couponNo,loginCodes);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("发券异常，异常信息：{}", e);
            return R.error("发券失败");
        }
    }

    /**
     * 获取券限制条件记录数
     */
    @RequestMapping("/count/{couponNo}")
    public boolean count(@PathVariable("couponNo") String couponNo){
        JSONObject params =  new JSONObject();
        params.put("couponNo", couponNo);
        return couponConditionDao.queryCount(params) > 0 ? true : false;
    }

    /**
     * 修改预待审核为预审核
     */
    @RequestMapping("/changeStatus/{couponNo}")
    public R changeStatus(@PathVariable("couponNo") String couponNo){
        CouponDetail couponDetail = new CouponDetail();
        couponDetail.setCouponNo(couponNo);

        try {
            couponDetailService.addCouponVerifyRecord(couponDetail);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("提交审核失败");
        }

        return R.ok();
    }

    /**
     * 图片上传
     */
    @RequestMapping("/uploadImg")
    public R uploadImg(MultipartHttpServletRequest request) {
        //获取文件
        Iterator<String> it = request.getFileNames();
        while (it.hasNext()) {
            MultipartFile file = request.getFile(it.next());
            String fileName = file.getOriginalFilename();
            // 图片格式过滤
            String suffix = fileName.substring(fileName.lastIndexOf("."));
            if (!".png".equals(suffix) && !".jpg".equals(suffix) && !".jpeg".equals(suffix) && !".bmp".equals(suffix)) {
                return R.error("请上传png/jpg/jpeg/bmp格式的图片!");
            }
        }
        //上传
        String path = "D:/img/";
        String fileName = FileUtil.saveImg(path, request);
        //返回响应
        Map<String, Object> res = new HashMap<>();
        if (null != fileName) {
            res.put("msg", "图片上传成功!");
            res.put("fileName", fileName);
            return R.ok(res);
        } else {
            return R.error("图片上传失败!");
        }
    }

}

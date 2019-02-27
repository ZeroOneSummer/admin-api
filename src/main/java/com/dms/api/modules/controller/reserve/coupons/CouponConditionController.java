package com.dms.api.modules.controller.reserve.coupons;


import com.dms.api.common.utils.PageUtils;
import com.dms.api.common.utils.Query;
import com.dms.api.common.utils.R;
import com.dms.api.modules.base.AbstractController;
import com.dms.api.modules.dao.reserve.coupons.CouponDetailDao;
import com.dms.api.modules.entity.reserve.coupons.CouponCondition;
import com.dms.api.modules.entity.sys.user.SysUserEntity;
import com.dms.api.modules.service.reserve.coupons.CouponConditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 优惠券限制条件
 * 
 * @author jp
 * @date 2018-06-29 15:22:02
 */

@RestController
@RequestMapping("couponcondition")
public class CouponConditionController extends AbstractController {

	@Autowired
	private CouponConditionService couponConditionService;

	@Autowired
	private CouponDetailDao couponDetailDao;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("couponcondition:list")
	public R list(@RequestParam Map<String, Object> params){

		Query query = new Query();
		query.putAll(params);

		List<CouponCondition> couponconditionList = couponConditionService.queryList(params);
		int total = couponConditionService.queryTotal(params);

		PageUtils pageUtil = new PageUtils(couponconditionList, total, query);
		
		return R.ok().put("page", pageUtil);
	}
	

	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	//@RequiresPermissions("couponcondition:info")
	public R info(@PathVariable("id") Integer id){
		CouponCondition couponCondition = couponConditionService.queryObject(id);
		
		return R.ok().put("couponCondition", couponCondition);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	//@RequiresPermissions("couponcondition:save")
	public R save(@RequestBody CouponCondition couponCondition){
		//验证券是否已审核通过(防止前端验证通过后再审核)
		String status = couponDetailDao.couponIsAudit(couponCondition.getCouponNo());
		if ("1".equals(status)) {
			return R.error("该券已审核，无法修改");
		}

		//检验条件是否重复
		if(couponConditionService.queryRepeat(couponCondition)) return R.error("该限制条件已存在");

		//获取用户信息
		SysUserEntity user = getUser();

		couponCondition.setCreateTime(new Date());
		couponCondition.setCreateUser(user.getUsername());
		couponConditionService.save(couponCondition);

		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	//@RequiresPermissions("couponcondition:update")
	public R update(@RequestBody CouponCondition couponCondition){
		//验证券是否已审核通过(防止前端验证通过后再审核)
		String status = couponDetailDao.couponIsAudit(couponCondition.getCouponNo());
		if ("1".equals(status)) {
			return R.error("该券已审核，无法修改");
		}

		//检验条件是否重复
		CouponCondition condition = couponConditionService.queryObject(couponCondition.getId()); //原本信息
		if ("LEVEL".equals(condition.getConditionType())){ //类型为用户等级时
			if (!(condition.getConditionValue()).equals(couponCondition.getConditionValue())){ //类型值发生了变化，需要检验
				if(couponConditionService.queryRepeat(couponCondition)) return R.error("该限制条件已存在");
			}
		}else{ //类型为AU、AG
			if (!(condition.getConditionType()).equals(couponCondition.getConditionType())){ //类型发生了变化，需要检验
				if(couponConditionService.queryRepeat(couponCondition)) return R.error("该限制条件已存在");
			}
		}

		//获取用户信息
		SysUserEntity user = getUser();

		couponCondition.setUpdateTime(new Date());
		couponCondition.setUpdateUser(user.getUsername());
		couponConditionService.update(couponCondition);

		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	//@RequiresPermissions("syslevelinfocycle:delete")
	public R delete(@RequestBody Integer[] ids){
		couponConditionService.deleteBatch(ids);

		return R.ok();
	}
	
}

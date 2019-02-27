package com.dms.api.modules.controller.reserve.level;


import com.dms.api.common.utils.*;
import com.dms.api.modules.base.AbstractController;
import com.dms.api.modules.entity.reserve.level.SysLevelapplyEntity;
import com.dms.api.modules.entity.sys.user.SysUserEntity;
import com.dms.api.modules.service.mall.user.DfMallUserInfoService;
import com.dms.api.modules.service.reserve.level.SysLevelapplyService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 等级申请表
 *
 * @author
 * @email @danpacmall.com
 * @date 2017-08-28 13:46:02
 */
@RestController
@RequestMapping("/syslevelapply")
public class SysLevelapplyController extends AbstractController {
	@Autowired
	private SysLevelapplyService sysLevelapplyService;

	@Autowired
	private DfMallUserInfoService dfMallUserInfoService;

	@Autowired
	private RedisUtils redisUtils;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("syslevelapply:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
		Query query = new Query(params);
		if (null != query.get("sidx") && "updTime".equals(query.get("sidx"))) query.put("sidx","updateTime");
		SysUserEntity user = getUser();
		if(!user.isSuperAdmin()){
			query.put("dealerId",null != user.getDealerId() ? user.getDealerId() : "unknown");
		}
		List<SysLevelapplyEntity> sysLevelapplyList = sysLevelapplyService.queryList(query);
		int total = sysLevelapplyService.queryTotal(query);

		PageUtils pageUtil = new PageUtils(sysLevelapplyList, total, query.getLimit(), query.getPage());

		return R.ok().put("page", pageUtil);
	}


	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("syslevelapply:info")
	public R info(@PathVariable("id") Integer id){
		SysLevelapplyEntity sysLevelapply = sysLevelapplyService.queryObject(id);

		return R.ok().put("sysLevelapply", sysLevelapply);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("syslevelapply:save")
	public R save(@RequestBody SysLevelapplyEntity sysLevelapply){
		sysLevelapply.setCreatetime(new Date());
		sysLevelapply.setOperator(getUserId());
		sysLevelapplyService.save(sysLevelapply);
		return R.ok();
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("syslevelapply:update")
	public R update(@RequestBody SysLevelapplyEntity sysLevelapply){

		//审核通过，更新用户等级
		if("APPLY_PASS".equals(sysLevelapply.getStatus())){
			//清空当前用户等级缓存20180626
			String cacheCurUserLevelKey= RedisKeys.RDS_USERLEVEL_CURLEVELINFO+sysLevelapply.getLogincode();
			logger.info("审核通过清空用户缓存key:{}",cacheCurUserLevelKey);
			redisUtils.delete(cacheCurUserLevelKey);
			dfMallUserInfoService.updateLevel(sysLevelapply);
		}

		sysLevelapply.setUpdatetime(new Date());
		sysLevelapply.setOperator(getUserId());
		sysLevelapplyService.update(sysLevelapply);
		return R.ok();
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("syslevelapply:delete")
	public R delete(@RequestBody Integer[] ids){
		sysLevelapplyService.deleteBatch(ids);

		return R.ok();
	}

}

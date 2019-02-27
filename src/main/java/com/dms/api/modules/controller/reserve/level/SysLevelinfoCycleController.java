package com.dms.api.modules.controller.reserve.level;


import com.dms.api.common.utils.PageUtils;
import com.dms.api.common.utils.R;
import com.dms.api.modules.entity.reserve.level.SysLevelinfoCycleEntity;
import com.dms.api.modules.service.reserve.level.SysLevelinfoCycleService;
import com.dms.api.modules.service.reserve.level.SysLevelinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 等级信息表
 * 
 * @author
 * @email @danpacmall.com
 * @date 2017-08-28 13:46:02
 */
@RestController
@RequestMapping("/syslevelinfocycle")
public class SysLevelinfoCycleController {

	@Autowired
	private SysLevelinfoCycleService sysLevelinfoCycleService;
	@Autowired
	private SysLevelinfoService sysLevelinfoService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("syslevelinfocycle:list")
	public R list(@RequestParam Map<String, Object> params){

		List<SysLevelinfoCycleEntity> sysLevelinfoList = sysLevelinfoCycleService.queryList(params);
		int total = 20;

		PageUtils pageUtil = new PageUtils(sysLevelinfoList, total, 20, 1);
		
		return R.ok().put("page", pageUtil);
	}
	

	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	//@RequiresPermissions("syslevelinfocycle:info")
	public R info(@PathVariable("id") Integer id){
		SysLevelinfoCycleEntity sysLevelinfoCycle = sysLevelinfoCycleService.queryObject(id);
		
		return R.ok().put("sysLevelinfocycle", sysLevelinfoCycle);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	//@RequiresPermissions("syslevelinfocycle:save")
	public R save(@RequestBody SysLevelinfoCycleEntity sysLevelinfoCycle){
		sysLevelinfoCycle.setCreateTime(new Date());
		sysLevelinfoCycleService.save(sysLevelinfoCycle);
		sysLevelinfoService.clearUserLevelCache();
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	//@RequiresPermissions("syslevelinfocycle:update")
	public R update(@RequestBody SysLevelinfoCycleEntity sysLevelinfoCycle){
		sysLevelinfoCycle.setUpdateTime(new Date());
		sysLevelinfoCycleService.update(sysLevelinfoCycle);
		sysLevelinfoService.clearUserLevelCache();
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	//@RequiresPermissions("syslevelinfocycle:delete")
	public R delete(@RequestBody Integer[] ids){
		sysLevelinfoCycleService.deleteBatch(ids);
		sysLevelinfoService.clearUserLevelCache();
		return R.ok();
	}
	
}

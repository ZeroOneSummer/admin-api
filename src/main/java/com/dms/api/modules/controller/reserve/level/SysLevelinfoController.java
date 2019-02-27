package com.dms.api.modules.controller.reserve.level;


import com.dms.api.common.utils.PageUtils;
import com.dms.api.common.utils.Query;
import com.dms.api.common.utils.R;
import com.dms.api.modules.entity.reserve.level.SysLevelinfoEntity;
import com.dms.api.modules.service.reserve.level.SysLevelinfoCycleService;
import com.dms.api.modules.service.reserve.level.SysLevelinfoService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 等级信息表
 *
 * @author
 * @email
 * @date
 */
@RestController
@RequestMapping("/syslevelinfo")
public class SysLevelinfoController {

	@Autowired
	private SysLevelinfoService sysLevelinfoService;

	@Autowired
	private SysLevelinfoCycleService sysLevelinfoCycleService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("syslevelinfo:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
		Query query = new Query(params);

		List<SysLevelinfoEntity> sysLevelinfoList = sysLevelinfoService.queryList(query);
		int total = sysLevelinfoService.queryTotal(query);

		PageUtils pageUtil = new PageUtils(sysLevelinfoList, total, query.getLimit(), query.getPage());

		return R.ok().put("page", pageUtil);
	}

	/**
	 * 获取等级
	 */
	/**
	 * 获取等级枚举
	 */
	@RequestMapping("/getLevels")
	public R getLevels(){
		//查询列表数据
		List<SysLevelinfoEntity> list = sysLevelinfoService.queryList(null);
		return R.ok().put("list", list);
	}


	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("syslevelinfo:info")
	public R info(@PathVariable("id") Integer id){
		SysLevelinfoEntity sysLevelinfo = sysLevelinfoService.queryObject(id);

		return R.ok().put("sysLevelinfo", sysLevelinfo);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("syslevelinfo:save")
	public R save(@RequestBody SysLevelinfoEntity sysLevelinfo){
		sysLevelinfo.setCreatetime(new Date());
		sysLevelinfoService.save(sysLevelinfo);
		if("Y".equals(sysLevelinfo.getIsDefault())){
			sysLevelinfoService.updateDfaultLevel(sysLevelinfo.getLevelcode());
		}
		sysLevelinfoService.clearUserLevelCache();
		return R.ok();
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("syslevelinfo:update")
	public R update(@RequestBody SysLevelinfoEntity sysLevelinfo){
		sysLevelinfo.setUpdatetime(new Date());
		sysLevelinfoService.update(sysLevelinfo);
		if("Y".equals(sysLevelinfo.getIsDefault())){
			sysLevelinfoService.updateDfaultLevel(sysLevelinfo.getLevelcode());
		}
		sysLevelinfoService.clearUserLevelCache();
		return R.ok();
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("syslevelinfo:delete")
	public R delete(@RequestBody Integer[] ids){
		try {
			//查询父IDs对应的子IDs
			String strIds = sysLevelinfoService.getCycleIdsByIds(ids);
			if (strIds != null && strIds != ""){
				String[] ids2 = strIds.split(",");
				Integer[] ids3 = new Integer[ids2.length];
				for(int i = 0; i < ids3.length; i++){
					ids3[i] = Integer.parseInt(ids2[i]);
				}
				//删除子IDs
				sysLevelinfoCycleService.deleteBatch(ids3);
			}
			//删除父IDs
			sysLevelinfoService.deleteBatch(ids);
		} catch (Exception e) {
			e.printStackTrace();
			return R.error("操作失败");
		}
		sysLevelinfoService.clearUserLevelCache();
		return R.ok();
	}

	/**
	 * 获取默认等级
	 */
	@RequestMapping("/getDefaultLevel")
	public R getDefaultLevel(){
		SysLevelinfoEntity sysLevelinfoEntity=sysLevelinfoService.getDefaultLevel();
		return R.ok().put("sysLevelinfoEntity", sysLevelinfoEntity);
	}

}

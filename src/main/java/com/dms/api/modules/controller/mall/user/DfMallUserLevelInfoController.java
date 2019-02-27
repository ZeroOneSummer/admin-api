package com.dms.api.modules.controller.mall.user;

import com.dms.api.common.utils.PageUtils;
import com.dms.api.common.utils.Query;
import com.dms.api.common.utils.R;
import com.dms.api.modules.base.AbstractController;
import com.dms.api.modules.entity.mall.user.DfMallUserLevelInfoEntity;
import com.dms.api.modules.entity.sys.user.SysUserEntity;
import com.dms.api.modules.service.mall.user.DfMallUserLevelInfoService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 商城用户等级表
 */
@RestController
@RequestMapping("dfmalluserlevelinfo")
public class DfMallUserLevelInfoController extends AbstractController {

	@Autowired
	private DfMallUserLevelInfoService dfMallUserLevelInfoService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("dfmalluserlevelinfo:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		SysUserEntity user = getUser();
		if(!user.isSuperAdmin()){
			query.put("dealerId",null != user.getDealerId() ? user.getDealerId() : "unknown");
		}
		List<DfMallUserLevelInfoEntity> dfMallUserLevelInfoList = dfMallUserLevelInfoService.queryList(query);
		int total = dfMallUserLevelInfoService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(dfMallUserLevelInfoList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}

	/**
	 * 获取等级枚举
	 */
	@RequestMapping("/getLevels")
	public R getLevels(){
		//查询列表数据
		List<DfMallUserLevelInfoEntity> dfMallUserLevelInfoList = dfMallUserLevelInfoService.queryList(null);
		return R.ok().put("list", dfMallUserLevelInfoList);
	}
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("dfmalluserlevelinfo:info")
	public R info(@PathVariable("id") Long id){
		DfMallUserLevelInfoEntity dfMallUserLevelInfo = dfMallUserLevelInfoService.queryObject(id);
		
		return R.ok().put("dfMallUserLevelInfo", dfMallUserLevelInfo);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("dfmalluserlevelinfo:save")
	public R save(@RequestBody DfMallUserLevelInfoEntity dfMallUserLevelInfo){
		dfMallUserLevelInfoService.save(dfMallUserLevelInfo);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("dfmalluserlevelinfo:update")
	public R update(@RequestBody DfMallUserLevelInfoEntity dfMallUserLevelInfo){
		dfMallUserLevelInfoService.update(dfMallUserLevelInfo);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("dfmalluserlevelinfo:delete")
	public R delete(@RequestBody Long[] ids){
		dfMallUserLevelInfoService.deleteBatch(ids);
		
		return R.ok();
	}
	
}

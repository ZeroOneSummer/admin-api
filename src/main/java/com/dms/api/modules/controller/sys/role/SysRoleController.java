package com.dms.api.modules.controller.sys.role;

import com.dms.api.modules.base.AbstractController;
import com.dms.api.common.annotation.SysLog;
import com.dms.api.modules.entity.sys.role.SysRoleEntity;
import com.dms.api.modules.service.sys.role.SysRoleMenuService;
import com.dms.api.modules.service.sys.role.SysRoleService;
import com.dms.api.common.utils.PageUtils;
import com.dms.api.common.utils.Query;
import com.dms.api.common.utils.R;
import com.dms.api.common.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 角色管理
 */
@RestController
@RequestMapping("/sys/role")
public class SysRoleController extends AbstractController {

	@Autowired
	private SysRoleService sysRoleService;

	@Autowired
	private SysRoleMenuService sysRoleMenuService;
	
	/**
	 * 角色列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("sys:role:list")
	public R list(@RequestParam Map<String, Object> params){
		//如果不是超级管理员，则只查询自己创建的角色列表
		if(!getUser().isSuperAdmin()){
			params.put("createUserId", getUserId());
		//如果是子超管，则不可查看子超管类型的角色
		}else if (!getUser().isSysSuperAdmin()) {
			params.put("roleType",1);
		}
		
		//查询列表数据
		Query query = new Query(params);
		List<SysRoleEntity> list = sysRoleService.queryList(query);
		int total = sysRoleService.queryTotal(query);
		PageUtils pageUtil = new PageUtils(list, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	/**
	 * 角色列表
	 */
	@RequestMapping("/select")
	@RequiresPermissions("sys:role:select")
	public R select(){
		Map<String, Object> map = new HashMap<>();
		
		//如果不是超级管理员，则只查询自己所拥有的角色列表
		if(!getUser().isSuperAdmin()){
			map.put("createUserId", getUserId());
		//如果是子超管，则不可查看子超管类型的角色
		} else if (!getUser().isSysSuperAdmin()) {
			map.put("roleType", 1);
		}
		List<SysRoleEntity> list = sysRoleService.queryList(map);

		return R.ok().put("list", list);
	}

	/**
	 * 角色信息
	 */
	@RequestMapping("/info/{roleId}")
	@RequiresPermissions("sys:role:info")
	public R info(@PathVariable("roleId") Long roleId){
		SysRoleEntity role = sysRoleService.queryObject(roleId);
		
		//查询角色对应的菜单
		List<Long> menuIdList = sysRoleMenuService.queryMenuIdList(roleId);
		role.setMenuIdList(menuIdList);
		
		return R.ok().put("role", role);
	}
	
	/**
	 * 保存角色
	 */
	@SysLog("保存角色")
	@RequestMapping("/save")
	@RequiresPermissions("sys:role:save")
	public R save(@RequestBody SysRoleEntity role){
		ValidatorUtils.validateEntity(role);

		role.setCreateUserId(getUserId());
		//如果是新增超管角色
		if (role.getRoleType() == 0) {
			if (!getUser().isSysSuperAdmin()) {
				R.error("你没有创建超管角色的权限！");
			}
		} else {
			role.setRoleType(1);
		}
		sysRoleService.save(role);

		return R.ok();
	}
	
	/**
	 * 修改角色
	 */
	@SysLog("修改角色")
	@RequestMapping("/update")
	@RequiresPermissions("sys:role:update")
	public R update(@RequestBody SysRoleEntity role){
		ValidatorUtils.validateEntity(role);
		
		role.setCreateUserId(getUserId());
		sysRoleService.update(role);
		
		return R.ok();
	}
	
	/**
	 * 删除角色
	 */
	@SysLog("删除角色")
	@RequestMapping("/delete")
	@RequiresPermissions("sys:role:delete")
	public R delete(@RequestBody Long[] roleIds){
		sysRoleService.deleteBatch(roleIds);
		
		return R.ok();
	}

}

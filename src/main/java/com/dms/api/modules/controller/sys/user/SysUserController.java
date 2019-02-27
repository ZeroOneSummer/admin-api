package com.dms.api.modules.controller.sys.user;

import com.alibaba.fastjson.JSONObject;
import com.dms.api.common.annotation.SysLog;
import com.dms.api.common.utils.Constant;
import com.dms.api.common.utils.PageUtils;
import com.dms.api.common.utils.Query;
import com.dms.api.common.utils.R;
import com.dms.api.common.validator.Assert;
import com.dms.api.common.validator.ValidatorUtils;
import com.dms.api.common.validator.group.AddGroup;
import com.dms.api.common.validator.group.UpdateGroup;
import com.dms.api.modules.base.AbstractController;
import com.dms.api.modules.entity.sys.user.SysUserEntity;
import com.dms.api.modules.service.sys.user.SysUserRoleService;
import com.dms.api.modules.service.sys.user.SysUserService;
import org.apache.commons.lang.ArrayUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 系统用户
 * @author
 * @email @danpacmall.com
 * @date 2016年10月31日 上午10:40:10
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController extends AbstractController {

	@Autowired
	private SysUserService sysUserService;

	@Autowired
	private SysUserRoleService sysUserRoleService;

	/**
	 * 所有用户列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("sys:user:list")
	public R list(@RequestParam Map<String, Object> params){
		//只有超级管理员，才能查看所有管理员列表
        logger.info("params:",params);
		if(!getUser().isSuperAdmin()){
			params.put("createUserId", getUserId());
		}else if (!getUser().isSysSuperAdmin()) {
			params.put("userType",1);
		}

		//查询列表数据
		Query query = new Query(params);
		List<SysUserEntity> userList = sysUserService.queryList(query);
		int total = sysUserService.queryTotal(query);

		PageUtils pageUtil = new PageUtils(userList, total, query.getLimit(), query.getPage());

		return R.ok().put("page", pageUtil);
	}

	/**
	 * 获取登录的用户信息
	 */
	@RequestMapping("/info")
	public R info(){
		return R.ok().put("user", getUser());
	}

	/**
	 * 修改登录用户密码
	 */
	/*@SysLog("修改密码")
	@RequestMapping("/password")
	public R password(String password, String newPassword){
		Assert.isBlank(newPassword, "新密码不为能空");

		//sha256加密
		password = new Sha256Hash(password, getUser().getSalt()).toHex();
		//sha256加密
		newPassword = new Sha256Hash(newPassword, getUser().getSalt()).toHex();

		//更新密码
		int count = sysUserService.updatePassword(getUserId(), password, newPassword);
		if(count == 0){
			return R.error("原密码不正确");
		}

		return R.ok();
	}*/
	@SysLog("修改密码")
	@RequestMapping("/password")
	public R password(@RequestBody JSONObject passWordParams) {
		logger.info("passWordParams,{}",passWordParams);
		Assert.isBlank(passWordParams.getString("newPassword").toString(), "新密码不为能空");

		//sha256加密
		String password = new Sha256Hash(passWordParams.getString("password").toString(), getUser().getSalt()).toHex();
		//sha256加密
		String newPassword = new Sha256Hash(passWordParams.getString("newPassword").toString(), getUser().getSalt()).toHex();

		//更新密码
		int count = sysUserService.updatePassword(getUserId(), password, newPassword);
		if (count == 0) {
			return R.error("原密码不正确");
		}
		return R.ok();
	}


	/**
	 * 用户信息
	 */
	@RequestMapping("/info/{userId}")
//	@RequiresPermissions("sys:user:info")
	public R info(@PathVariable("userId") Long userId){
		SysUserEntity user = sysUserService.queryObject(userId);
		user.setAdminEd(user.isSuperAdmin()|| user.isSysSuperAdmin());
		//获取用户所属的角色列表
		List<Long> roleIdList = sysUserRoleService.queryRoleIdList(userId);
		user.setRoleIdList(roleIdList);

		return R.ok().put("user", user);
	}

	/**
	 * 保存用户
	 */
	@SysLog("保存用户")
	@RequestMapping("/save")
	@RequiresPermissions("sys:user:save")
	public R save(@RequestBody SysUserEntity user) {
		if (!user.isAdminEd()) {
			user.setUserType(Constant.DEFAULT_USER_TYPE);
		} else {
			//如果不是超管则不能创建子超管
			if (!getUser().isSysSuperAdmin()) {
				return R.error("你没有创建超管用户的权限！");
			}
			//创建超管角色
			user.setOrgId(null);
			user.setDealerId(null);
			user.setUserType(Constant.SUPER_ADMIN);
		}
		ValidatorUtils.validateEntity(user, AddGroup.class);

		user.setCreateUserId(getUserId());
		sysUserService.save(user);

		return R.ok();
	}

	/**
	 * 修改用户
	 */
	@SysLog("修改用户")
	@RequestMapping("/update")
	@RequiresPermissions("sys:user:update")
	public R update(@RequestBody SysUserEntity user){
		SysUserEntity loginUser = getUser();
		if (!loginUser.isSuperAdmin()){
			user.setCreateUserId(getUserId());
		}
		if (user.isAdminEd()){
			//如果不是超管则不能创建子超管
			if (!loginUser.isSysSuperAdmin()) {
				return R.error("你没有创建超管用户的权限！");
			}
			user.setUserType(Constant.SYS_SUPER_ADMIN == user.getUserType() ? Constant.SYS_SUPER_ADMIN : Constant.SUPER_ADMIN);
			user.setOrgId(null);
			user.setOrgId(null);
		}
		ValidatorUtils.validateEntity(user, UpdateGroup.class);
		sysUserService.update(user);

		return R.ok();
	}

	/**
	 * 删除用户
	 */
	@SysLog("删除用户")
	@RequestMapping("/delete")
	@RequiresPermissions("sys:user:delete")
	public R delete(@RequestBody Long[] userIds){
		if(ArrayUtils.contains(userIds, 1L)){
			return R.error("系统管理员不能删除");
		}

		if(ArrayUtils.contains(userIds, getUserId())){
			return R.error("当前用户不能删除");
		}

		sysUserService.deleteBatch(userIds);

		return R.ok();
	}


	/**
	 * 设置审批密码（仅限管理员）
	 */
	@RequestMapping("/addOrUpdatePwd")
	@RequiresPermissions("sys:user:addOrUpdatePwd")
	public R addOrUpdatePwd(@RequestBody SysUserEntity user) {
		return sysUserService.addOrUpdatePwd(user);
	}

	/**
	 * 设置审批密码（仅限服务商）
	 */
	@RequestMapping("/sendOrAckSms")
	public R sendOrAckSms(@RequestBody SysUserEntity user) {
		//获取当前用户信息
		user.setMobile(getUser().getMobile());
		user.setUserId(getUser().getUserId());

		return sysUserService.sendOrAckSms(user);
	}

	/**
	 * 重置审批密码
	 */
	@RequestMapping("/reset")
	@RequiresPermissions("sys:user:resetPwd")
	public R reset(@RequestBody SysUserEntity user){

		if (sysUserService.resetAuditPwd(user) > 0)
			return R.ok();
		else
			return R.error("操作失败");
	}

}

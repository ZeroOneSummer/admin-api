package com.dms.api.modules.controller.mall.user;

import com.dms.api.common.Enum.FieldEnum;
import com.dms.api.common.utils.PageUtils;
import com.dms.api.common.utils.Query;
import com.dms.api.common.utils.R;
import com.dms.api.common.utils.StringUtils;
import com.dms.api.modules.base.AbstractController;
import com.dms.api.modules.entity.mall.user.DfMallUserInfoEntity;
import com.dms.api.modules.entity.mall.user.UserInfoEntity;
import com.dms.api.modules.entity.sys.user.SysUserEntity;
import com.dms.api.modules.service.mall.user.DfMallUserInfoService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 商城用户表
 */
@RestController
@RequestMapping("dfmalluserinfo")
public class DfMallUserInfoController extends AbstractController {

	@Autowired
	private DfMallUserInfoService dfMallUserInfoService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("dfmalluserinfo:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

//		List<DfMallUserInfoEntity> dfMallUserInfoList = dfMallUserInfoService.queryList(query);
		SysUserEntity user = getUser();
		if(!user.isSuperAdmin()){
			query.put("dealerId",null != user.getDealerId() ? user.getDealerId() : "unknown");
		}
		List<Map> list=dfMallUserInfoService.getUserAndLevel(query);
		int total = dfMallUserInfoService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(list, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("dfmalluserinfo:info")
	public R info(@PathVariable("id") Long id){
		DfMallUserInfoEntity dfMallUserInfo = dfMallUserInfoService.queryObject(id);
		return R.ok().put("dfMallUserInfo", dfMallUserInfo);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("dfmalluserinfo:save")
	public R save(@RequestBody DfMallUserInfoEntity dfMallUserInfo){
		dfMallUserInfo.setGmtCreate(new Date());
		dfMallUserInfoService.save(dfMallUserInfo);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("dfmalluserinfo:update")
	public R update(@RequestBody DfMallUserInfoEntity dfMallUserInfo){
		dfMallUserInfo.setGmtModified(new Date());
		dfMallUserInfoService.update(dfMallUserInfo);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("dfmalluserinfo:delete")
	public R delete(@RequestBody Long[] ids){
		dfMallUserInfoService.deleteBatch(ids);
		return R.ok();
	}


	/**
	 * 客户信息列表查询
	 */
	@RequestMapping("/userinfoList")
	@RequiresPermissions("dfmalluserinfo:userinfo:list")
	public R userList (@RequestParam Map<String, Object> params){

		R result = new R ();
		PageUtils pageUtil = null;

		logger.info("客户信息列表查询");
		Query query = new Query(params);
		SysUserEntity user = getUser();

		String levelCondition = (String)query.get("level");
		if (! StringUtils.isBlank(levelCondition)){
			FieldEnum fieldEnum = FieldEnum.valueOf(levelCondition);
			if(null != fieldEnum){
				String leveName = fieldEnum.getCondition();
				query.put("levelName",leveName);
			}
		}

		List<UserInfoEntity> list= dfMallUserInfoService.queryuUserInfoList(query);
		dataShield(list);


		int total = dfMallUserInfoService.queryuUserInfoListTotal(query);

		pageUtil = new PageUtils(list, total, query.getLimit(), query.getPage());

		return R.ok().put("page", pageUtil);
	}

	/**
	 * 屏蔽返回前端的数据
	 * @return
	 */
	private void dataShield(List<UserInfoEntity> list ){
		if (null != list && list.size() > 0){
			for (UserInfoEntity e:list) {
				String phone = e.getPhone();
				if (! StringUtils.isBlank(phone)){
					e.setPhone(StringUtils.phoneShield(phone));
				}
			}
		}
	}

}

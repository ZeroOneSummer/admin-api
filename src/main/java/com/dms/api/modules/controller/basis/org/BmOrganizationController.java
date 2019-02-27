package com.dms.api.modules.controller.basis.org;

import com.dms.api.common.utils.PageUtils;
import com.dms.api.common.utils.Query;
import com.dms.api.common.utils.R;
import com.dms.api.common.utils.StringUtils;
import com.dms.api.core.constants.ConstantTable;
import com.dms.api.modules.base.AbstractController;
import com.dms.api.modules.entity.basis.org.BmDealerInfo;
import com.dms.api.modules.entity.basis.org.BmOrganizationEntity;
import com.dms.api.modules.entity.basis.org.BmSysOrganization;
import com.dms.api.modules.entity.basis.org.BmSysOrganizationWithParent;
import com.dms.api.modules.service.basis.org.BmDealerInfoService;
import com.dms.api.modules.service.basis.org.BmOrganizationService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 机构表
 * 
 */
@RestController
@RequestMapping("/org/bmorganization")
public class BmOrganizationController extends AbstractController {

	@Autowired
	private BmOrganizationService bmOrganizationService;

	@Autowired
	private BmDealerInfoService bmDealerInfoService;

	/**
	 * 根据用户信息查询机构树
	 * @return
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("org:bmorganization:list")
	public List<BmSysOrganization> list(){
		List<BmSysOrganization> queryList = bmOrganizationService.queryListByUser(getUser());
		return queryList;
	}

	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("bmorganization:info")
	public R info(@PathVariable("id") Long id){
		BmOrganizationEntity bmOrganization = bmOrganizationService.queryObject(id);
		
		return R.ok().put("bmOrganization", bmOrganization);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("org:bmorganization:save")
	public R save(@RequestBody BmOrganizationEntity bmOrganization){
		bmOrganizationService.save(bmOrganization);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("org:bmorganization:update")
	public R update(@RequestBody BmOrganizationEntity bmOrganization){
		bmOrganizationService.update(bmOrganization);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("org:bmorganization:delete")
	public R delete(@RequestBody Long[] ids){
		bmOrganizationService.deleteBatch(ids);
		
		return R.ok();
	}

	/**
	 * 指定订货分部下属机构
	 * @return
	 */
	@RequestMapping("/orgs")
	@RequiresPermissions("org:bmorganization:level:list")
	public R orgLevelList(@RequestParam Map<String,Object > param){
		R result = new R();
		Query queryParam = new Query(param);

		String levelStr = (String) queryParam.get("level");
		if (StringUtils.isBlank(levelStr)){
			//默认获取所有层别下属机构
			queryParam.put("level", ConstantTable.orglevel0);
		}

		String dealerCode = (String) queryParam.get("dealerCode");
		if ( ! StringUtils.isBlank(dealerCode)){
			//获取服务商ID
			Map<String,Object> paramTemp = new HashMap<>(2);
			paramTemp.put("dealerCode",dealerCode);
			paramTemp.put("dealerType",3);
			List<BmDealerInfo> bmDealerInfos = bmDealerInfoService.queryList(paramTemp);
			if (null != bmDealerInfos  && bmDealerInfos.size() > 0){
				BmDealerInfo bmDealerInfo = bmDealerInfos.get(0);
				queryParam.put("dealerId",bmDealerInfo.getDealerId());
			}
		}

		List<BmSysOrganization> bmSysOrganizations = bmOrganizationService.queryListInLevel(queryParam);
		result.put("data",bmSysOrganizations);
		return result;
	}

	/**
	 * 指定订货分部下属机构
	 * @return
	 */
	@RequestMapping("/orgs/info")
	@RequiresPermissions("org:bmorganization:level:list")
	public R orgListwithInfo(@RequestParam Map<String,Object > param){
		R result = new R();
		Query queryParam = new Query(param);
		queryParam.put("limit",1000);

		String levelStr = (String) queryParam.get("level");
		if (StringUtils.isBlank(levelStr)){
			//默认获取所有层别下属机构
			queryParam.put("level",ConstantTable.orglevel0);
		}

		String dealerCode = (String) queryParam.get("dealerCode");
		if ( ! StringUtils.isBlank(dealerCode)){
			//获取服务商ID
			Map<String,Object> paramTemp = new HashMap<>(2);
			paramTemp.put("dealerCode",dealerCode);
			paramTemp.put("dealerType",3);
			List<BmDealerInfo> bmDealerInfos = bmDealerInfoService.queryList(paramTemp);
			if (null != bmDealerInfos  && bmDealerInfos.size() > 0){
				BmDealerInfo bmDealerInfo = bmDealerInfos.get(0);
				queryParam.put("dealerId",bmDealerInfo.getDealerId());
			}
		}

		List<BmSysOrganizationWithParent> bmSysOrganizations = bmOrganizationService.queryListInLevelWith(queryParam);
		bmOrganizationService.setUserCountWithOrg(bmSysOrganizations);

		int total = bmOrganizationService.queryListInLevelTotal(queryParam);
		PageUtils pageUtil = new PageUtils(bmSysOrganizations, total, queryParam.getLimit(), queryParam.getPage());

		result.put("page",pageUtil);
		return result;
	}
	
}

package com.dms.api.modules.controller.sys.config;

import com.dms.api.common.annotation.SysLog;
import com.dms.api.common.redis.PubChannelConfig;
import com.dms.api.common.utils.PageUtils;
import com.dms.api.common.utils.Query;
import com.dms.api.common.utils.R;
import com.dms.api.common.utils.RedisService;
import com.dms.api.common.validator.ValidatorUtils;
import com.dms.api.modules.base.AbstractController;
import com.dms.api.core.constants.ConstantTable;
import com.dms.api.modules.entity.sys.config.SysConfigEntity;
import com.dms.api.modules.service.sys.config.SysConfigService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统参数信息
 */
@RestController
@RequestMapping("/sys/config")
public class SysConfigController extends AbstractController {

	@Autowired
	PubChannelConfig pub;

	@Autowired
	private SysConfigService sysConfigService;

	@Autowired
	private RedisService redisService;
	
	/**
	 * 所有配置列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("sys:config:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
		Query query = new Query(params);
		List<SysConfigEntity> configList = sysConfigService.queryList(query);
		int total = sysConfigService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(configList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}

	/**
	 * 配置信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("sys:config:info")
	public R info(@PathVariable("id") Long id){
		SysConfigEntity config = sysConfigService.queryObject(id);
		
		return R.ok().put("config", config);
	}
	
	/**
	 * 保存配置
	 */
	@SysLog("保存配置")
	@RequestMapping("/save")
	@RequiresPermissions("sys:config:save")
	public R save(@RequestBody SysConfigEntity config){
		ValidatorUtils.validateEntity(config);

		sysConfigService.save(config);

		Long pid = config.getPid();
		String pkey = ConstantTable.CONFIG_CACHE_KEY_COMOMN;
		if ( -1L != pid){
			SysConfigEntity sysConfigEntity = sysConfigService.queryObject(pid);
			pkey = sysConfigEntity.getKey();
		}

		//放缓存
		String key = config.getKey();
		String value = config.getValue();
		Map<String ,String > cachemap = new HashMap<>(1);
		cachemap.put(key,value);

		redisService.setMap(pkey,cachemap);

		//发布配置信息
		String pubValue = pkey + ConstantTable.PUB_CONFIG_VALUE_SPLIT + key;
		pub.pubConfig(pubValue);

		return R.ok();
	}
	
	/**
	 * 修改配置
	 */
	@SysLog("修改配置")
	@RequestMapping("/update")
	@RequiresPermissions("sys:config:update")
	public R update(@RequestBody SysConfigEntity config){
		ValidatorUtils.validateEntity(config);
		
		sysConfigService.update(config);

		Long pid = config.getPid();
		String pkey = ConstantTable.CONFIG_CACHE_KEY_COMOMN;
		if ( -1L != pid){
			SysConfigEntity sysConfigEntity = sysConfigService.queryObject(pid);
			pkey = sysConfigEntity.getKey();
		}

		//放缓存
		String key = config.getKey();
		String value = config.getValue();
		Map<String ,String > cachemap = new HashMap<>(1);
		cachemap.put(key,value);

		redisService.setMap(pkey,cachemap);

		//发布配置信息
		String pubValue = pkey + ConstantTable.PUB_CONFIG_VALUE_SPLIT + key;
		pub.pubConfig(pubValue);

		return R.ok();
	}
	
	/**
	 * 删除配置
	 */
	@SysLog("删除配置")
	@RequestMapping("/delete")
	@RequiresPermissions("sys:config:delete")
	public R delete(@RequestBody Long[] ids){
		sysConfigService.deleteBatch(ids);
		return R.ok();
	}
    
	/**
	 * 获取pid下拉列表
	 */
	@RequestMapping("/listPid")
	public R listPid() {
		List<SysConfigEntity> listPid=sysConfigService.listPid();
		return R.ok().put("data", listPid);
	}

	/**
	 * 获取系统时间
	 */
	@RequestMapping("/currentTime")
	public Date currentTime(){
		return new Date();
	}

}

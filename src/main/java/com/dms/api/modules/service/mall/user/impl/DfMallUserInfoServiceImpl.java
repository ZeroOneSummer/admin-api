package com.dms.api.modules.service.mall.user.impl;

import com.alibaba.fastjson.JSONObject;
import com.dms.api.common.utils.HttpClientUtil;
import com.dms.api.common.utils.Query;
import com.dms.api.common.utils.StringUtils;
import com.dms.api.core.config.HostsConfig;
import com.dms.api.core.constants.ConstantTable;
import com.dms.api.modules.dao.mall.user.DfMallUserInfoDao;
import com.dms.api.modules.entity.mall.user.DfMallUserInfoEntity;
import com.dms.api.modules.entity.mall.user.UserInfoEntity;
import com.dms.api.modules.entity.reserve.level.SysLevelapplyEntity;
import com.dms.api.modules.service.mall.user.DfMallUserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("dfMallUserInfoService")
public class DfMallUserInfoServiceImpl implements DfMallUserInfoService {
	private Logger logger= LoggerFactory.getLogger(getClass());

	@Autowired
	private DfMallUserInfoDao dfMallUserInfoDao;

	@Override
	public DfMallUserInfoEntity queryObject(Long id){
		return dfMallUserInfoDao.queryObject(id);
	}
	
	@Override
	public List<DfMallUserInfoEntity> queryList(Map<String, Object> map){
		return dfMallUserInfoDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return dfMallUserInfoDao.queryTotal(map);
	}
	
	@Override
	public void save(DfMallUserInfoEntity dfMallUserInfo){
		dfMallUserInfoDao.save(dfMallUserInfo);
	}
	
	@Override
	public void update(DfMallUserInfoEntity dfMallUserInfo){
		dfMallUserInfoDao.update(dfMallUserInfo);
	}
	
	@Override
	public void delete(Long id){
		dfMallUserInfoDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Long[] ids){
		dfMallUserInfoDao.deleteBatch(ids);
	}

	@Override
	public List<Map> getUserAndLevel(Query query) {
		return dfMallUserInfoDao.getUserAndLevel(query);
	}

	@Override
	public void updateLevel(SysLevelapplyEntity sysLevelapply) {
		DfMallUserInfoEntity dfMallUserInfoEntity=dfMallUserInfoDao.getUserInfo(sysLevelapply);

		if(dfMallUserInfoEntity!=null){
			dfMallUserInfoDao.updateLevel(sysLevelapply);
			logger.info("用户等级审核 发券配置的接口 接口IP:{}",HostsConfig.hostsUserLevel);
			try {
				if(StringUtils.isNotBlank(HostsConfig.hostsUserLevel)) {
					String url = HostsConfig.hostsUserLevel + ConstantTable.SEND_COUPON;
					Map<String, String> param = new HashMap<>();
					param.put("loginCode", sysLevelapply.getLogincode());
					param.put("targetLevelCode", sysLevelapply.getTargetlevelcode());
					logger.info("用户等级审核通过调用用户等级 接口发券: 请求 url:{}  param:{}", url, JSONObject.toJSONString(param));
					String result = HttpClientUtil.doPost(url, param, 3000);
					logger.info("用户等级审核通过调用用户等级 接口发券: 响应:{}", result);
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("用户等级审核通过调用用户等级接口异常", e);
			}

		}else{
			logger.info("审核没有查询到用户信息:{}",JSONObject.toJSONString(sysLevelapply));
		}
	}

	@Override
	public List<UserInfoEntity> queryuUserInfoList(Query param) {
		return dfMallUserInfoDao.queryuUserInfoList(param);
	}

	@Override
	public int queryuUserInfoListTotal(Query param) {
		return dfMallUserInfoDao.queryuUserInfoListTotal(param);
	}

}

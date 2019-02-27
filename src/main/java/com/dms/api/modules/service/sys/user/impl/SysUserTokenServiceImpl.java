package com.dms.api.modules.service.sys.user.impl;

import com.dms.api.common.utils.R;
import com.dms.api.core.oauth2.TokenGenerator;
import com.dms.api.modules.service.sys.user.SysUserTokenService;
import com.dms.api.modules.dao.sys.user.SysUserTokenDao;
import com.dms.api.modules.entity.sys.user.SysUserTokenEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service("sysUserTokenService")
public class SysUserTokenServiceImpl implements SysUserTokenService {

	private final static int EXPIRE = 3600 * 12; //12小时后过期

	@Autowired
	private SysUserTokenDao sysUserTokenDao;

	@Override
	public SysUserTokenEntity queryByUserId(Long userId) {
		return sysUserTokenDao.queryByUserId(userId);
	}

	@Override
	public void save(SysUserTokenEntity token){
		sysUserTokenDao.save(token);
	}
	
	@Override
	public void update(SysUserTokenEntity token){
		sysUserTokenDao.update(token);
	}

	@Override
	public R createToken(long userId) {
		//生成一个token
		String token = TokenGenerator.generateValue();

		//当前时间
		Date now = new Date();
		//过期时间
		Date expireTime = new Date(now.getTime() + EXPIRE * 1000);

		//判断是否生成过token
		SysUserTokenEntity tokenEntity = queryByUserId(userId);
		if(tokenEntity == null){
			tokenEntity = new SysUserTokenEntity();
			tokenEntity.setUserId(userId);
			tokenEntity.setToken(token);
			tokenEntity.setUpdateTime(now);
			tokenEntity.setExpireTime(expireTime);

			//保存token
			save(tokenEntity);
		}else{
			tokenEntity.setToken(token);
			tokenEntity.setUpdateTime(now);
			tokenEntity.setExpireTime(expireTime);

			//更新token
			update(tokenEntity);
		}

		R r = R.ok().put("token", token).put("expire", EXPIRE);

		return r;
	}

	@Override
	public void logout(long userId) {
		//生成一个token
		String token = TokenGenerator.generateValue();

		//修改token
		SysUserTokenEntity tokenEntity = new SysUserTokenEntity();
		tokenEntity.setUserId(userId);
		tokenEntity.setToken(token);
		update(tokenEntity);
	}

	@Override
	public boolean isOnline(String token) {

		SysUserTokenEntity sysUserTokenEntity = sysUserTokenDao.queryByToken(token);
		if(null == sysUserTokenEntity){
			return false;
		}

		Date now = new Date();
		Date expireTime = sysUserTokenEntity.getExpireTime();

		if(now.getTime() > expireTime.getTime()){
			return false;
		}

		return true;
	}

}

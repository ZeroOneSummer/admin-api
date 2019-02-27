package com.dms.api.modules.service.sys.user;

import com.dms.api.modules.entity.sys.user.SysUserTokenEntity;
import com.dms.api.common.utils.R;

/**
 * 用户Token
 * @author
 * @email @danpacmall.com
 * @date 2017-03-23 15:22:07
 */
public interface SysUserTokenService {

	SysUserTokenEntity queryByUserId(Long userId);

	void save(SysUserTokenEntity token);
	
	void update(SysUserTokenEntity token);

	/**
	 * 生成token
	 */
	R createToken(long userId);

	/**
	 * 退出，修改token值
	 */
	void logout(long userId);

	boolean isOnline(String token);

}

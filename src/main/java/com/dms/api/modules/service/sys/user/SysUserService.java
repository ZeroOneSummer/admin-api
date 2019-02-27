package com.dms.api.modules.service.sys.user;

import com.dms.api.common.utils.R;
import com.dms.api.modules.entity.sys.user.SysUserEntity;

import java.util.List;
import java.util.Map;

/**
 * 系统用户
 * @author
 * @email @danpacmall.com
 * @date 2016年9月18日 上午9:43:39
 */
public interface SysUserService {

	/**
	 * 查询用户的所有权限
	 * @param userId  用户ID
	 */
	List<String> queryAllPerms(Long userId);
	
	/**
	 * 查询用户的所有菜单ID
	 */
	List<Long> queryAllMenuId(Long userId);

	/**
	 * 根据用户名，查询系统用户
	 */
	SysUserEntity queryByUserName(String username);
	
	/**
	 * 根据用户ID，查询用户
	 */
	SysUserEntity queryObject(Long userId);
	
	/**
	 * 查询用户列表
	 */
	List<SysUserEntity> queryList(Map<String, Object> map);
	
	/**
	 * 查询总数
	 */
	int queryTotal(Map<String, Object> map);
	
	/**
	 * 保存用户
	 */
	void save(SysUserEntity user);
	
	/**
	 * 修改用户
	 */
	void update(SysUserEntity user);
	
	/**
	 * 删除用户
	 */
	void deleteBatch(Long[] userIds);
	
	/**
	 * 修改密码
	 */
	int updatePassword(Long userId, String password, String newPassword);

	/**
	 *设置审批密码（仅限管理员）
	 */
	R addOrUpdatePwd(SysUserEntity user);

	/**
	 *发送或确认短信验证码
	 */
	R sendOrAckSms(SysUserEntity user);

	/**
	 * 重置审批密码
	 */
	int resetAuditPwd(SysUserEntity user);

}

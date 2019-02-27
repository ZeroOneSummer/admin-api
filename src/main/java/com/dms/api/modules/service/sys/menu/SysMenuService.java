package com.dms.api.modules.service.sys.menu;

import com.dms.api.modules.entity.sys.menu.MenuEntity;
import com.dms.api.modules.entity.sys.menu.SysMenuEntity;
import com.dms.api.modules.entity.sys.user.SysUserEntity;

import java.util.List;
import java.util.Map;

/**
 * 菜单管理
 * @author
 * @email @danpacmall.com
 * @date 2016年9月18日 上午9:42:16
 */
public interface SysMenuService {
	
	/**
	 * 根据父菜单，查询子菜单
	 */
	List<MenuEntity> queryListParentId(Long parentId, List <Long> menuIdList);

	/**
	 * 根据父菜单，查询子菜单
	 */
	List<MenuEntity> queryListParentId(Long parentId);
	
	/**
	 * 获取不包含按钮的菜单列表
	 */
	List<SysMenuEntity> queryNotButtonList();
	
	/**
	 * 获取用户菜单列表
	 */
	List<MenuEntity> getUserMenuList(SysUserEntity user);

	/**
	 * 获取用户菜单列表
	 */
	List<SysMenuEntity> queryUserMenuList(Long userId);
	
	/**
	 * 查询菜单
	 */
	SysMenuEntity queryObject(Long menuId);
	
	/**
	 * 查询菜单列表
	 */
	List<SysMenuEntity> queryList(Map <String, Object> map);
	
	/**
	 * 查询总数
	 */
	int queryTotal(Map <String, Object> map);
	
	/**
	 * 保存菜单
	 */
	void save(SysMenuEntity menu);
	
	/**
	 * 修改
	 */
	void update(SysMenuEntity menu);
	
	/**
	 * 删除
	 */
	void deleteBatch(Long[] menuIds);
	
	/**
	 * 查询用户的权限列表
	 */
	List<SysMenuEntity> queryUserList(Long userId);

}

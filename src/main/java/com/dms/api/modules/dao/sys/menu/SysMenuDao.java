package com.dms.api.modules.dao.sys.menu;

import com.dms.api.modules.base.BaseDao;
import com.dms.api.modules.entity.sys.menu.MenuEntity;
import com.dms.api.modules.entity.sys.menu.SysMenuEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 菜单管理
 * 
 */
@Mapper
public interface SysMenuDao extends BaseDao<SysMenuEntity> {
	
	/**
	 * 根据父菜单，查询子菜单
	 * @param parentId 父菜单ID
	 */
	List<MenuEntity> queryListParentId(Long parentId);
	
	/**
	 * 获取不包含按钮的菜单列表
	 */
	List<SysMenuEntity> queryNotButtonList();
	
	/**
	 * 查询用户的权限列表
	 */
	List<SysMenuEntity> queryUserList(Long userId);

	/**
	 * 查询用户的权限列表
	 */
	List<SysMenuEntity> queryUserMenuList(Long userId);
}

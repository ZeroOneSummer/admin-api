package com.dms.api.modules.service.sys.menu.impl;

import com.dms.api.modules.dao.sys.menu.SysMenuDao;
import com.dms.api.modules.entity.sys.menu.MenuEntity;
import com.dms.api.modules.entity.sys.menu.SysMenuEntity;
import com.dms.api.modules.entity.sys.user.SysUserEntity;
import com.dms.api.modules.service.sys.menu.SysMenuService;
import com.dms.api.modules.service.sys.user.SysUserService;
import com.dms.api.common.utils.Constant.MenuType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service("sysMenuService")
public class SysMenuServiceImpl implements SysMenuService {

	@Autowired
	private SysMenuDao sysMenuDao;

	@Autowired
	private SysUserService sysUserService;
	
	@Override
	public List<MenuEntity> queryListParentId(Long parentId, List<Long> menuIdList) {
		List<MenuEntity> menuList = queryListParentId(parentId);
		if(menuIdList == null){
			return menuList;
		}
		
		List<MenuEntity> userMenuList = new ArrayList<>();
		for(MenuEntity menu : menuList){
			if(menuIdList.contains(menu.getMenuId())){
				userMenuList.add(menu);
			}
		}
		return userMenuList;
	}

	@Override
	public List<MenuEntity> queryListParentId(Long parentId) {
		return sysMenuDao.queryListParentId(parentId);
	}

	@Override
	public List<SysMenuEntity> queryNotButtonList() {
		return sysMenuDao.queryNotButtonList();
	}

	@Override
	public List<MenuEntity> getUserMenuList(SysUserEntity user) {
		//系统管理员，拥有最高权限
		if(user.isSysSuperAdmin()){
			return getAllMenuList(null);
		}
		
		//用户菜单列表
		List<Long> menuIdList = sysUserService.queryAllMenuId(user.getUserId());	//用户所有菜单
		return getAllMenuList(menuIdList);
	}

	@Override
	public List<SysMenuEntity> queryUserMenuList(Long userId) {
		//用户对应角色可分配的权限菜单列表
		List<SysMenuEntity> menuList = sysMenuDao.queryUserMenuList(userId);
		return menuList;
	}
	
	@Override
	public SysMenuEntity queryObject(Long menuId) {
		return sysMenuDao.queryObject(menuId);
	}

	@Override
	public List<SysMenuEntity> queryList(Map<String, Object> map) {
		return sysMenuDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return sysMenuDao.queryTotal(map);
	}

	@Override
	public void save(SysMenuEntity menu) {
		sysMenuDao.save(menu);
	}

	@Override
	public void update(SysMenuEntity menu) {
		sysMenuDao.update(menu);
	}

	@Override
	@Transactional
	public void deleteBatch(Long[] menuIds) {
		sysMenuDao.deleteBatch(menuIds);
	}
	
	@Override
	public List<SysMenuEntity> queryUserList(Long userId) {
		return sysMenuDao.queryUserList(userId);
	}

	/**
	 * 获取所有菜单列表
	 */
	private List<MenuEntity> getAllMenuList(List<Long> menuIdList){
		//查询根菜单列表
		List<MenuEntity> menuList = queryListParentId(0L, menuIdList);
		//递归获取子菜单
		getMenuTreeList(menuList, menuIdList);
		
		return menuList;
	}

	/**
	 * 递归
	 */
	private List<MenuEntity> getMenuTreeList(List<MenuEntity> menuList, List<Long> menuIdList){
		List<MenuEntity> subMenuList = new ArrayList<MenuEntity>();
		
		for(MenuEntity entity : menuList){
			if(entity.getType() == MenuType.CATALOG.getValue()){//目录
				entity.setChildren(getMenuTreeList(queryListParentId(entity.getMenuId(), menuIdList), menuIdList));
			}
			subMenuList.add(entity);
		}
		
		return subMenuList;
	}

}

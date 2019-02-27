package com.dms.api.modules.entity.sys.menu;

import com.dms.api.common.utils.StringUtils;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 新S菜单管理
 */
@Data
public class MenuEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long menuId;			//菜单ID
	private Long parentId;			//父菜单ID
	private String path;			//页面名称
	private String name;			//菜单名称
	private String component;		//菜单url
	private Integer type;			//菜单类型（0-目录，1-菜单，2-按钮）
	private MetaEntity meta = new MetaEntity();		//附加信息实体
	private String icon;			//附加信息
	private String title;			//附加信息
	private Integer orderNum;		//菜单排序
	private List<?> children;		//子菜单集合


	public void setIcon(String icon) {
		meta.setIcon(icon);
		this.icon = icon;
	}

	public void setTitle(String title) {
		meta.setTitle(title);
		this.title = title;
	}

	public void setComponent(String component) {
		if (parentId == 0){
			this.component = "Layout";
			this.path = "/" + component;
		}else{
			this.component = component;
			this.path = component;
		}
	}
}

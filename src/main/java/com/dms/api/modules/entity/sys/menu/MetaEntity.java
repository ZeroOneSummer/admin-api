package com.dms.api.modules.entity.sys.menu;

import lombok.Data;

import java.io.Serializable;

/**
 * meta附加信息
 */
@Data
public class MetaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private String title;
	private String icon;
}

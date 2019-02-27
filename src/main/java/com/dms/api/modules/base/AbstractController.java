package com.dms.api.modules.base;

import com.dms.api.modules.entity.sys.user.SysUserEntity;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author 40
 * Controller公共组件
 */
public abstract class AbstractController {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	protected SysUserEntity getUser() {
		return (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
	}

	protected Long getUserId() {
		return getUser().getUserId();
	}

	 /**
	  * 验证参数
	  */
	protected boolean validate(Object object, Class clzz, String... columns){
		switch (clzz.getSimpleName()){
			case "Map":
				for (String key: columns) {
					if(null == ((Map)object).get(key)){
						return false;
					}
				}
				return true;
			default:
				for (String columnName : columns){
					try {
						Field field = clzz.getField(columnName);
						field.setAccessible(true);
						Object value = field.get(object);
						if(null == value){
							return false;
						}
					}catch (NoSuchFieldException e){
						logger.error(clzz.getSimpleName()+"不存在参数【"+columnName+"】");
					}catch (IllegalAccessException e){
						logger.error(e.getMessage());
					}
				}
		}

		return true;
	}

}

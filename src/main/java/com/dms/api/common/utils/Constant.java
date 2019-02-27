package com.dms.api.common.utils;

/**
 * 常量
 * 
 */
public class Constant {

	/** 超级管理员 */
	public static final int SYS_SUPER_ADMIN = -1;

	/** 子超管 */
	public static final int SUPER_ADMIN = 0;

    /** 超管默认机构id */
    public static final Long SUPER_ADMIN_ORG_ID = 10000L;

    /** 普通用户类型 */
    public static final int DEFAULT_USER_TYPE = 1;



    /**
	 * 菜单类型
	 * 
	 */
    public enum MenuType {
        /**
         * 目录
         */
    	CATALOG(0),
        /**
         * 菜单
         */
        MENU(1),
        /**
         * 按钮
         */
        BUTTON(2);

        private int value;

        private MenuType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 定时任务状态
     */
    public enum ScheduleStatus {
        /**
         * 正常
         */
        NORMAL(0),
        /**
         * 暂停
         */
        PAUSE(1);

        private int value;

        private ScheduleStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
  

}

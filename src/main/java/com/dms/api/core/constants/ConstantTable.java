package com.dms.api.core.constants;

/**
 * 常量统一配置类
 */
public class ConstantTable {

	/**
	 * 缓存相关
	 */
	public static final String GLOBO_REDIS_KEY_PRIXX = "SYS:DFMALL:";				//商城项目名称
	public static final String PROJECT_LAUNCH_DAY = "2017-08-12";					//商城项目上线时间
	public static final String MENU_REDIS_KEY_PREFIX = "SYS:MENU:";	  				//菜单缓存key
	public static final String USER_INFO_REDIS_KEY_PREFIX = "SYS:USER_INFO:";	    //用户信息缓存key
	public static final String OPEN_CLOSE_CACHE_KEY = "SYS:DFMALL:OPENCLOSECONFIG";	//预订缓存key
	public static final String OPEN_CLOSE_MAP_KEY = "configs";	  					//预订缓存mapkey
	public static final  String MERCHANT="MERCHANT:";	  							//厂商缓存
	public static final  String MERCHANT_DEALER_B="MERCHANT:DEALER:B:";	  			//预定厂商获取商家key
	public static final  String MERCHANT_DEALER_S="MERCHANT:DEALER:S:";	  			//回购厂商获取商家key
	public static final String CONFIG_CACHE_KEY_COMOMN = "nop";	  					//没有父级的配置缓存key(key)
	public static final String CONFIG_CACHE_KEY_SHIFT_ORG = "SHRFTORG";	  			//机构转移时间配置缓存key(key)
	public static final String CONFIG_CACHE_KEY_SHIFT_ORG_END_TIME = "shiftorg-endtime";	  	//机构转移截止时间缓存key(field)
	public static final String CONFIG_CACHE_KEY_SHIFT_ORG_START_TIME = "shiftorg-starttime";	//机构转移开始时间缓存key(field)


	/**
	 * 日期相关
	 */
	public static final String CURRENTDAY="CURRENTDAY";    							//当天
	public static final String YESTERODAY="YESTERODAY";    							//昨天
	public static final String CURRENTWEEK="CURRENTWEEK";  							//本周
	public static final String PREWEEK="PREWEEK";          							//上周
	public static final String CURRENTMONTH="CURRENTMONTH";  						//本月
	public static final String PREMONTH="PREMONTH";          						//上月


	/**
	 * 用户等级相关
	 */
	public static final String level0_5 = "level0_5"; //0.5级==体验用户
	public static final String level0 = "level0";	  //0级==未开通
	public static final String level1 = "level1";	  //1级==一级商家
	public static final String level2 = "level2";	  //2级==二级商家
	public static final String level3 = "level3";	  //3级==三级商家
	public static final String level4 = "level4";	  //4级==四级商家
	public static final String level5 = "level5";	  //5级==五级商家


	/**
	 * 机构相关
	 */
	public static final String orglevel0 = "0";	  						  //所有下属机构
	public static final String orglevel1 = "1";	  						  //所有下属机构
	public static final String orglevel2 = "2";	  						  //所有下属机构
	public static final String orglevel3 = "3";	  						  //所有下属机构
	public static final String orglevel4 = "4";	  						  //所有下属机构
	public static final String orglevel5 = "5";	  						  //所有下属机构
	public static final String SHIFT_ORG_VERIFY = "SHIFT_ORG";	  		  //机构审核
	public static final String CONFIG_ORGLEVELCOUNT = "LEVEL_COUNT";	  //所有机构层数
	public static final String ORG_LEVEL_VIEW_PREFIX = "org_level_";	  //机构级别视图表前缀


	/**
	 * 接口相关
	 */
	//审批
	public static final String GET_MYAUDIT = "/getMyAudit";	  							//获取我的审核列表
	public static final String PASS_MYAUDIT = "/passMyAudit";	  						//审核通过
	public static final String ADD_USERS = "/addUsers";	  								//添加用户
	public static final String GET_USERS_LIST = "/getUsersList";	  					//获取用户列表
	public static final String UPT_USERS_GROUP = "/upUsersGroup";	  					//更新用户组别
	public static final String GET_GROUP = "/getGroup";	  								//获取用户组别列表
	public static final String ADD_VAC = "/addVac";	  									//添加申请
	public static final String GET_MYVAC = "/getMyVac";	  								//获取我的申请
	public static final String GET_VAC_RECORD = "/getMyVacRecord";  					//获取我的申请的历史订单
	public static final String GET_VAC_HISTORY = "/getMyVacHistory"; 					//获取我审批的历史订单查询
	public static final String GET_PROCDEF = "/getProcdef";								//获获取审核组别列表
	//转机构
	public static final String USER_ORG_MOVE = "/user_org_move";	  					//转机构
	//退差价
	public static final String REFUND_API = "/return/disparity";	  					//退差价划账
	//回购
	public static final String GET_ORDER = "/api/user/order/getOrder";	  				//获取订单(回购单、全款单)
	public static final String BACK_PAY = "/api/trade/backPay/relevant";	  			//回购资金划转
	public static final String BUY_BACK_REVIEW = "/api/user/s/buybackReview/update";	//回购审批状态同步到A系统
	public static final String SEND_SMS = "/sendSms";	  								//回购审批状态同步到A系统
	//优惠券
	public static final String SEND_COUPON = "/UserLevel/verifyUserLevel/sendCoupon";	//发券
	//商城用户
	public static final String UPT_USER_PHONE = "/api/user/s/phone/update";				//修改用户手机号
	//对公出金
	public static final String BINDCARD_LIST = "/api/user/bank/trade/bank/biz/enterprise/query";			//绑卡信息列表
	public static final String BIND_CARD = "/api/user/bank/trade/bank/biz/enterprise/bind";					//绑卡
	public static final String UNBIND_CARD = "/api/user/bank/trade/bank/biz/enterprise/unbind";				//解绑
	public static final String WITHDRAW_SEND = "/api/user/bank/trade/auth/withdraw/biz/enterprise/send";	//提现申请
	public static final String WITHDRAW_ACK = "/api/user/bank/trade/auth/withdraw/biz/enterprise/ack";		//提现确认
	public static final String WITHDRAW_CANCEL = "/api/user/bank/trade/auth/withdraw/biz/enterprise/cancel";//提现驳回
	public static final String LOGINQUERY_ALLOWOWWITHDRAWAL = "/api/user/loginS";//查询服务商可出资金



	/**
	 * 其他
	 */
	public static final String REFUND_APPLY_VERIFY = "REFUND_APPLY";	 			//退差价审核
	public static final String CONFIG_CHANNEL = "CONFIG:CHANNEL:SAVEORUPDATE";	  	//系统配置频道前缀
	public static final String PUB_CONFIG_VALUE_SPLIT = ":";	  					//配置信息的发布值分隔符

}

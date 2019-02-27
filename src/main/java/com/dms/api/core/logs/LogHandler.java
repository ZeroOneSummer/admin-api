package com.dms.api.core.logs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 业务日志标准格式类
 * @author GHOST
 */
public class LogHandler {

	static final String SPL = "|";
	
	private static final Logger logger = LoggerFactory.getLogger(LogHandler.class);

	public static void logPrint(HttpServletRequest request, String operate, LogType type, String msg) {
		PageData pd=new PageData(request);
		String param=pd.toString();
		String msgStr=operate+SPL+param+SPL+msg+SPL;
		logPrint(type, msgStr);
	}

	public static void getLogControllerException(HttpServletRequest request, String operate, LogType type, Exception e){
		PageData pd=new PageData(request);
		String param=pd.toString();
		StackTraceElement stack=e.getStackTrace()[0];
		String exceptionMsg=stack.getClassName()+"."+stack.getMethodName()+stack.getLineNumber();
		String msgStr=operate+SPL+exceptionMsg+SPL+param+SPL+e.toString()+e.getMessage()+SPL;
		logPrint(type, msgStr);
	}


	public static void getLogController(HttpServletRequest request, String operate, LogType type, String url, String param){
		PageData pd=new PageData(request);
		param+="["+pd+"]";
		String token = request.getHeader("token");
		String msgStr=operate+SPL+""+SPL+url+SPL+param+SPL+token;
		logPrint(type, msgStr);
	}

	public static void logIsysInfo(LogType type, String url, Map<String, ?> headers,Map<String, ?> params, String result,String time){
		Object token = headers.get("token");
		String msgStr="I-SYS"+SPL+url+SPL+params+SPL+result+SPL+token+SPL+time;
		logPrint(type, msgStr);
	}

	public static void logAsysInfo(LogType type, String url, Map<String, ?> headers,Map<String, ?> params, String result,String time){
		String msgStr="A-SYS"+SPL+url+SPL+params+SPL+result+SPL+SPL+time;
		logPrint(type, msgStr);
	}

	public static void logAsysInfo(LogType type, String msg){
		PageData pd=new PageData();
		String params ="["+pd+"]";
		String msgStr="A-SYS"+SPL+params+SPL+msg+SPL;
		logPrint(type, msgStr);
	}

	public static void logAsysInfo(LogType type,String params, String msg,String time){
		String msgStr="A-SYS"+SPL+params+SPL+msg+SPL+SPL+time;
		logPrint(type, msgStr);
	}


	public static void logCsysInfo(LogType type, String url, Map<String, ?> headers, Map<String, ?> params, String result){
		String msgStr="C-SYS"+SPL+url+SPL+headers+SPL+params+SPL+result+SPL;
		logPrint(type, msgStr);
	}

	public static void logCsysInfo(LogType type, String url, Map<String, ?> params, String result){
		String msgStr="C-SYS"+SPL+url+SPL+params+SPL+result+SPL;
		logPrint(type, msgStr);
	}
	
	private static String getNowDate(){
		return timeStamp2Date(System.currentTimeMillis()+"",null);
	}
	
	 /** 
       * 时间戳转换成日期格式字符串 
       * @param seconds 精确到秒的字符串 
       * @param format
       * @return 
      */  
     private static String timeStamp2Date(String seconds, String format) {
         if(seconds == null || seconds.isEmpty() || "null".equals(seconds)){
             return "";  
         }  
		if(format == null || format.isEmpty()){
			format = "yyyy-MM-dd HH:mm:ss";
		}   
		SimpleDateFormat sdf = new SimpleDateFormat(format);  
		return sdf.format(new Date(Long.valueOf(seconds)));  
	 }
	
	/**
	 * 根据不同类型打印不同日志
	 * @param type 类型
	 * @param content 内容
	 */
	private static void logPrint(LogType type, String content) {
		content = entryPassword(content);
		if(type.equals(LogType.D)){
			logger.debug(content);
		}else if(type.equals(LogType.W)){
			logger.warn(content);
		}else if(type.equals(LogType.E) || type.equals(LogType.ALL)){
			logger.error(content);
		}else {
			logger.info(content);
		}
	}

    /**
     * 密码参数日志加密输出
     * **/
    static String entryPassword(String text){
        try {
            // 1，登录密码
            text = text.replaceAll( "(?<=loginPWD=)[^,}]*", "******");
            // 2，修改登录密码（旧密码/新密码）
            text = text.replaceAll( "(?<=\"oldPassword\":\")[^\",}]*", "******");
            text = text.replaceAll( "(?<=\"newPassword\":\")[^\",}]*", "******");
            // 3，修改支付密码（新密码）
            text = text.replaceAll( "(?<=\"accpwd\":\")[^\",}]*", "******");
            // 4，资金划转支付密码
            text = text.replaceAll( "(?<=monyPwd=)[^,}]*", "******");
            text = text.replaceAll( "(?<=\"monyPwd\":\")[^\",}]*", "******");
            // 5 微信授权获取openid
			text = text.replaceAll( "(?<=secret=)[^,}]*", "******");
			text = text.replaceAll( "(?<=appid=)[^,}]*", "******");
			text = text.replaceAll( "(?<=password=)[^,}]*", "******");
			text = text.replaceAll( "(?<=\"password\":\")[^\",}]*", "******");
        }catch (Exception ignored){ }
        return text;
    }

    /**
	 * 去除空格与换行
	 * */
	static String compress(String json)
	{
		if (json == null)
		{
			return null;
		}
		StringBuilder sb = new StringBuilder();
		boolean skip = true;// true 允许截取(表示未进入string双引号)
		boolean escaped = false;// 转义符
		for (int i = 0; i < json.length(); i++)
		{
			char c = json.charAt(i);
			escaped = !escaped && c == '\\';
			if (skip)
			{
				if (c == ' ' || c == '\r' || c == '\n' || c == '\t' )
				{
					continue;
				}
			}
			sb.append(c);
			if (c == '"')
			{
				skip = !skip;
			}
		}
		return sb.toString();
	}
}

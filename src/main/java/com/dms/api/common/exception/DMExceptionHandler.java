package com.dms.api.common.exception;

import com.dms.api.common.utils.R;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

/**
 * 异常处理器
 */
@RestControllerAdvice
public class DMExceptionHandler {
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 自定义异常
	 */
	@ExceptionHandler(DMException.class)
	public R handleRRException(DMException e){
		R r = new R();
		r.put("code", e.getCode());
		r.put("msg", e.getMessage());

		return r;
	}

	@ExceptionHandler(DuplicateKeyException.class)
	public R handleDuplicateKeyException(DuplicateKeyException e){
		logger.error(e.getMessage(), e);
		return R.error("数据库中已存在该记录");
	}

	@ExceptionHandler(AuthorizationException.class)
	public R handleAuthorizationException(AuthorizationException e){
		logger.error(e.getMessage(), e);
		return R.error("没有权限，请联系管理员授权");
	}

	@ExceptionHandler(ConnectTimeoutException.class)
	public R handleConnectTimeoutException(ConnectTimeoutException e){
		logger.error(e.getMessage(), e);
		return R.error("网络连接异常，请联系管理员！");
	}
	@ExceptionHandler(ConnectException.class)
	public R handleConnectException(ConnectException e){
		logger.error(e.getMessage(), e);
		return R.error("网络连接异常，请联系管理员！");
	}
	@ExceptionHandler(SocketTimeoutException.class)
	public R handleSocketTimeoutException(SocketTimeoutException e){
		logger.error(e.getMessage(), e);
		return R.error("网络繁忙，请稍后重试！");
	}

	@ExceptionHandler(Exception.class)
	public R handleException(Exception e){
		logger.error(e.getMessage(), e);
		return R.error();
	}
}

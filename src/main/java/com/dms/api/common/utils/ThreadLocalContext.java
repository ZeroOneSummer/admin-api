/*
 * 
 * LegendShop 多用户商城系统
 * 
 *  版权所有,并保留所有权利。
 * 
 */
package com.dms.api.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * 系统运行上下文.
 */
public class ThreadLocalContext {

	/** The log. */
	private final static Logger log = LoggerFactory.getLogger(ThreadLocalContext.class);

	/** The locale resolver. */
	private static LocaleResolver localeResolver;


	/** The request holder. */
	private static ThreadLocal<HttpServletRequest> requestHolder = new ThreadLocal<>();


	/**
	 * Clean.
	 */
	public static void clean() {
		if (requestHolder.get() != null) {
			requestHolder.remove();
		}
	}

	/**
	 * 是否开始请求.
	 * 
	 * @return true, if successful
	 */
	public static boolean requestStarted() {
		return requestHolder.get() != null;
	}

	/**
	 * Gets the locale.
	 * 
	 * @return the locale
	 */
	public static Locale getLocale() {
		HttpServletRequest request = requestHolder.get();
		if (request == null) {
			return null;
		}
		return localeResolver.resolveLocale(request);
	}

	/**
	 * Gets the request.
	 * 
	 * @return the request
	 */
	public static HttpServletRequest getRequest() {
		return requestHolder.get();
	}

	/**
	 * Sets the request.
	 * 
	 * @param request
	 *            the new request
	 */
	public static void setRequest(HttpServletRequest request) {
		requestHolder.set(request);
	}

}

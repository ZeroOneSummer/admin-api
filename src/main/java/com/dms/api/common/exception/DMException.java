package com.dms.api.common.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * 自定义异常
 * 
 */
@Getter
@Setter
public class DMException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
    private String msg;
    private int code = 500;
    
    public DMException(String msg) {
		super(msg);
		this.msg = msg;
	}
	
	public DMException(String msg, Throwable e) {
		super(msg, e);
		this.msg = msg;
	}
	
	public DMException(String msg, int code) {
		super(msg);
		this.msg = msg;
		this.code = code;
	}
	
	public DMException(String msg, int code, Throwable e) {
		super(msg, e);
		this.msg = msg;
		this.code = code;
	}
}

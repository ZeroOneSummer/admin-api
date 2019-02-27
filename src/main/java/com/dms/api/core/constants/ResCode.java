package com.dms.api.core.constants;

public enum ResCode {

	Res_200(200,"success"),
	Res_500(500,"fail");
	
	private int code ;
	private String codeMsg;
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getCodeMsg() {
		return codeMsg;
	}

	public void setCodeMsg(String codeMsg) {
		this.codeMsg = codeMsg;
	}

	ResCode(int code,String codemsg){}
}

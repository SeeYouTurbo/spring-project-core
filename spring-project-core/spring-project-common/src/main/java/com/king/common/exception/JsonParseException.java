package com.king.common.exception;

import java.io.IOException;

/**
 * 
* @author xuyajie 
* @date 2018年7月18日 上午10:25:57 
* JSON转换异常
 */
public class JsonParseException extends IOException{

	private static final long serialVersionUID = -8976859790941860152L;

	public JsonParseException() {
		super();
	}

	public JsonParseException(String message, Throwable cause) {
		super(message, cause);
	}

	public JsonParseException(String message) {
		super(message);
	}

	public JsonParseException(Throwable cause) {
		super(cause);
	}
	
	
}
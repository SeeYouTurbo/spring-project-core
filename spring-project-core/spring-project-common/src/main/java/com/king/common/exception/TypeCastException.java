package com.king.common.exception;
/**
 * 类型转换异常
* @author xuyajie 
* @date 2017年6月12日 下午7:05:11 
 */
public class TypeCastException extends IllegalArgumentException {
    static final long serialVersionUID = -2848938806368998894L;

	public TypeCastException() {
		super();
	}

	public TypeCastException(String message, Throwable cause) {
		super(message, cause);
	}

	public TypeCastException(String s) {
		super(s);
	}

	public TypeCastException(Throwable cause) {
		super(cause);
	}
    
}
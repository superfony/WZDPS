package com.epsmart.wzdp.http.request;

public class BaseException extends Exception {
	private static final long serialVersionUID = 8024097983213125910L;

	private int code;

	public BaseException(int code) {
		super();
		this.code = code;
	}

	public BaseException(int code, String msg) {
		super(msg);
		this.code = code;
	}

	public BaseException(int code, String msg, Throwable throwable) {
		super(msg, throwable);
		this.code = code;
	}

	public BaseException(int code, Throwable throwable) {
		super(throwable);
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}

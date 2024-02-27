package com.kt.smp.common.exception;

/**
 * @title  예외처리
 * @author Brian
 * @since  2022. 01. 29.
 * @see
 * <pre>
 * </pre>
 */
public class DaoException extends RuntimeException {

	private static final long serialVersionUID = -6671907150679366362L;
	
	public static final int DEFAULT_ERROR_CODE = 99999;										// 디퐅트 에러코드
	private final int errorCode;															// 에러코드

	public DaoException(int errorCode) {
		this(errorCode, (String) null);
	}

	public DaoException(String message) {
		this(DEFAULT_ERROR_CODE, message);
	}

	public DaoException(int errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public DaoException(int errorCode, Throwable cause) {
		this(errorCode, null, cause);
	}

	public DaoException(String message, Throwable cause) {
		this(DEFAULT_ERROR_CODE, message, cause);
	}

	public DaoException(int errorCode, String message, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
	}
	
	/**
	 * @title 에러코드를 가져온다.
	 * @return int
	 */
	public int getErrorCode() {
		return this.errorCode;
	}
}
package com.kt.smp.common.exception;

/**
 * @title  예외처리
 * @author Brian
 * @since  2022. 01. 29.
 * @see
 * <pre>
 * </pre>
 */
public class SmpException extends Exception {

	private static final long serialVersionUID = -6395706685709502552L;

	public SmpException() {
		super();
	}

	public SmpException(String message) {
		super(message);
	}

	public SmpException(String message, Throwable t) {
		super(message, t);
	}
}
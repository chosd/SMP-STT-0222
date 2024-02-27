package com.kt.smp.common.exception;

/**
 * @title  예외처리
 * @author Brian
 * @since  2022. 01. 29.
 * @see
 * <pre>
 * </pre>
 */
public class FailException extends RuntimeException implements MessageHandler {

	private static final long serialVersionUID = -3673868490322487125L;
	
	public static final int DEFAULT_CODE = -1;
	private String msgCode;
	private boolean messagePriority = true;
	 
	public FailException(String message) {
		this(message, null,  true);
	}
	
	public FailException(String msgCode, Object[] format) {
		this(null, msgCode,  false);
	}
	
	public FailException(String message, String msgCode, Object[] format) {
		this(message, msgCode,  true);
	}

	public FailException(MessageHandler messageHandler) {
		this(messageHandler.getMessage(), messageHandler.getMsgCode(), messageHandler.isMessagePriority());
	}
	
	public FailException(String message, String msgCode, boolean messagePriority) {
		super(message);
		this.msgCode = msgCode;
		this.messagePriority = messagePriority;
	}

	@Override
	public int getCode() {
		return DEFAULT_CODE;
	}

	@Override
	public String getMsgCode() {
		return msgCode;
	}

	@Override
	public boolean isMessagePriority() {
		return messagePriority;
	}
}
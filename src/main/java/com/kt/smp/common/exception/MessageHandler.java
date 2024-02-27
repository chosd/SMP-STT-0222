package com.kt.smp.common.exception;

/**
 * @title  메시지 핸들러
 * @author Brian
 * @since  2022. 01. 29.
 * @see
 * <pre>
 * </pre>
 */
interface MessageHandler {

	int getCode();

	String getMessage();

	String getMsgCode();

	boolean isMessagePriority();
}
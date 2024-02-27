/**
 * 
 */
package com.kt.smp.stt.callinfo.exception;

import org.springframework.http.HttpStatus;

import com.kt.smp.stt.callinfo.enums.CallInfoError;

import lombok.Getter;

/**
* @FileName : CallInfoException.java
* @Project : STT_SMP_Service
* @Date : 2023. 10. 23.
* @작성자 : homin.lee
* @변경이력 :
* @프로그램설명 : 콜 정보 관련 예외
*/
@Getter
public class CallInfoException extends RuntimeException {
	private HttpStatus httpStatus;
	private String message;
	
	public CallInfoException(CallInfoError callInfoExceptionMessage) {
		this.message = callInfoExceptionMessage.getMessage();
		this.httpStatus = callInfoExceptionMessage.getHttpStatus();
	}
	
	public CallInfoException(CallInfoError callInfoExceptionMessage, Exception e) {
		super(e);
		this.message = callInfoExceptionMessage.getMessage();
		this.httpStatus = callInfoExceptionMessage.getHttpStatus();
	}
	
	public CallInfoException(String message, Exception e) {
		super(message, e);
	}

	
}

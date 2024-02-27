/**
 * 
 */
package com.kt.smp.stt.reprocess.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

/**
* @FileName : ReprocessException.java
* @Project : STT_SMP_Service
* @Date : 2023. 10. 24.
* @작성자 : homin.lee
* @변경이력 :
* @프로그램설명 :
*/
@Getter
public class ReprocessException extends RuntimeException {
	private HttpStatus httpStatus;
	private String message;
	
	public ReprocessException(ReprocessError reprocessError) {
		this.message = reprocessError.getMessage();
		this.httpStatus = reprocessError.getHttpStatus();
	}
	
	public ReprocessException(ReprocessError reprocessError, String message) {
		super(message);
		this.message = reprocessError.getMessage();
		this.httpStatus = reprocessError.getHttpStatus();
	}
	
	public ReprocessException(ReprocessError reprocessError, Exception e) {
		super(e);
		this.httpStatus = reprocessError.getHttpStatus();
		this.message = reprocessError.getMessage();
	}
	
	public ReprocessException(ReprocessError reprocessError, String message, Exception e) {
		super(message, e);
		this.httpStatus = reprocessError.getHttpStatus();
		this.message = reprocessError.getMessage();
	}
	
	public ReprocessException(String message, Exception e) {
		super(message, e);
	}

}

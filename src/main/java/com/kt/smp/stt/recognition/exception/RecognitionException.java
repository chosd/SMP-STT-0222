/**
 * 
 */
package com.kt.smp.stt.recognition.exception;

import org.springframework.http.HttpStatus;

import com.kt.smp.stt.recognition.enums.RecognitionError;

import lombok.Getter;

/**
* @FileName : RecognitionException.java
* @Project : STT_SMP_Service
* @Date : 2023. 10. 27.
* @작성자 : homin.lee
* @변경이력 :
* @프로그램설명 :
*/
@Getter
public class RecognitionException extends RuntimeException {
	private HttpStatus httpStatus;
	private String message;
	
	public RecognitionException(RecognitionError recognitionError) {
		this.message = recognitionError.getMessage();
		this.httpStatus = recognitionError.getHttpStatus();
	}
	
	public RecognitionException(RecognitionError recognitionError, Exception e) {
		super(e);
		this.message = recognitionError.getMessage();
		this.httpStatus = recognitionError.getHttpStatus();
	}
	
	public RecognitionException(String message, Exception e) {
		super(message, e);
	}

}
/**
 * 
 */
package com.kt.smp.stt.recognition.enums;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
* @FileName : RecognitionError.java
* @Project : STT_SMP_Service
* @Date : 2023. 10. 27.
* @작성자 : rapeech
* @변경이력 :
* @프로그램설명 :
*/
@RequiredArgsConstructor
@Getter
public enum RecognitionError {
	
	SERVICE_CODE_NOT_SELECTED(HttpStatus.BAD_REQUEST, "Service Code Not Selected");
	
	private final HttpStatus httpStatus;
	private final String message;
}

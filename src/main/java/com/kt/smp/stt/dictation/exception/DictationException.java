/**
 * 
 */
package com.kt.smp.stt.dictation.exception;


import lombok.Getter;

/**
* @FileName : DictationException.java
* @Project : STT_SMP_Service
* @Date : 2023. 12. 15.
* @작성자 : rapeech
* @변경이력 :
* @프로그램설명 : 전사데이터 관련 예외
*/
@Getter
public class DictationException extends RuntimeException {
	public DictationException(String message, Exception e) {
		super(message, e);
	}
	
	public DictationException(String message) {
		super(message);
	}
	
}
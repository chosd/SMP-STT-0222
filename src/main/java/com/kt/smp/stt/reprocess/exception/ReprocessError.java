/**
 * 
 */
package com.kt.smp.stt.reprocess.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
* @FileName : ReprocessError.java
* @Project : STT_SMP_Service
* @Date : 2023. 10. 24.
* @작성자 : homin.lee
* @변경이력 :
* @프로그램설명 :
*/
@RequiredArgsConstructor
@Getter
public enum ReprocessError {
	
	DUPLICATED_ALL(HttpStatus.CONFLICT, "선택한 모든 콜이 재처리 중입니다."), 
	FAIL_TO_LINK_REPROCESS_SERVER(HttpStatus.SERVICE_UNAVAILABLE, "재처리 서버 연동에 실패하였습니다."),
	DB_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "DB에 접근 혹은 쿼리를 실행하는 중 에러가 발생하였습니다."),
	FAIL_TO_LOAD_WAV_FILE(HttpStatus.INTERNAL_SERVER_ERROR, "음원 파일을 불러오는 데 실패하였습니다."),
	RESPONSE_FAIL_CODE(HttpStatus.INTERNAL_SERVER_ERROR, "준실시간 처리 요청에 실패하였습니다."),
	HISTORY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 콜에 대한 재처리 이력이 없습니다.")
	;	
	
	private final HttpStatus httpStatus;
	private final String message;
}

/**
 * 
 */
package com.kt.smp.stt.callinfo.enums;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
* @FileName : StreamResult.java
* @Project : STT_SMP_Service
* @Date : 2023. 10. 23.
* @작성자 : homin.lee
* @변경이력 :
* @프로그램설명 :
*/
@RequiredArgsConstructor
@Getter
public enum CallInfoError {
	
	FAIL_TO_LINK(HttpStatus.SERVICE_UNAVAILABLE, "녹취 서버와 연동에 실패하였습니다."),
	DB_IO_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "DB 조회에 실패하였습니다."),
	NO_FILE_IN_LINK_SERVER(HttpStatus.NOT_FOUND, "녹취 서버에 해당 음원 파일이 없습니다."),
	NO_FILE_IN_DATABASE(HttpStatus.NOT_FOUND, "DB에 음원파일경로가 존재하지 않습니다."),
	NO_FILE_IN_FILE_PATH(HttpStatus.NOT_FOUND, "해당경로에 음원파일이 존재하지 않습니다."),
	TIME_FORMAT_IS_WRONG(HttpStatus.INTERNAL_SERVER_ERROR, "대화록의 날짜 포맷이 잘못되었습니다."),
	CONVERSATION_NOT_EXIST(HttpStatus.NO_CONTENT, "현재 상담 어플리케이션 ID에 대한 대화록이 존재하지 않습니다."),
	FAIL_TO_DOWNLOAD(HttpStatus.INTERNAL_SERVER_ERROR, "음원 파일을 다운로드하는 중 오류가 발생하였습니다."),
	FAIL_TO_GET_CALL_STATUS(HttpStatus.INTERNAL_SERVER_ERROR, "상태 조회에 실패하였습니다."),
	FAIL_TO_CREATE_FOLDER(HttpStatus.INTERNAL_SERVER_ERROR, "폴더 생성 중 에러가 발생하였습니다.");
	
	private final HttpStatus httpStatus;
	private final String message;
}

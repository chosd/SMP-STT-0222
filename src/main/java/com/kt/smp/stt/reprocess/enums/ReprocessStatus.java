/**
 * 
 */
package com.kt.smp.stt.reprocess.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
* @FileName : ReprocessStatus.java
* @Project : STT_SMP_Service
* @Date : 2023. 10. 12.
* @작성자 : homin.lee
* @변경이력 :
* @프로그램설명 : Reprocess Status Enum class
*/
@RequiredArgsConstructor
@Getter
public enum ReprocessStatus {
	NOT_EXIST(0, "재처리 요청 없음", "Not Exist"),
	READY(1000, "요청 상태", "Ready"),
	PROGRESS(2000, "STT 변환중", "Progress"),
	SUCCESS(3000, "STT 변환완료", "Success"),
	FAIL(4000, "STT 변환실패", "Fail"),
	SEND_SUCCESS(5000,"전송 완료", "Send Success"),
	SEND_FAIL(6000,"전송 실패", "Send Fail");
	
	private final Integer statusCode;

	@JsonValue  // 클라이언트로 한글 상태 리턴됨
	private final String statusNameKor;
	
	private final String statusNameEng;
}

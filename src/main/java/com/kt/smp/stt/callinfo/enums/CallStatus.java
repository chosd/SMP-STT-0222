/**
 * 
 */
package com.kt.smp.stt.callinfo.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
* @FileName : CallStatusEnum.java
* @Project : STT_SMP_Service
* @Date : 2023. 10. 12.
* @작성자 : homin.lee
* @변경이력 :
* @프로그램설명 : Call status Enum class
*/
@RequiredArgsConstructor
@Getter
public enum CallStatus {
	START(1000, "시작", "Start"),
	PROGRESS(2000, "진행중", "Progress"),
	END(3000, "완료", "End"),
	REPROCESS(4000, "재처리", "reprocess");
	
	@JsonValue  // 클라이언트로 코드가 리턴됨
	private final Integer statusCode;
	
	private final String statusNameKor;
	
	private final String statusNameEng;
}

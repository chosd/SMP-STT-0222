/**
 * 
 */
package com.kt.smp.stt.comm.preference.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PreferenceCodeKey {
	SCHEDULER("S")					// 스케줄러
	,ETC("E")						// 기타 설정
	,THRESHOLD("T")					// HW 리소스 모니터링 임계치
	,AGENT("A")						// AGENT 관련 설정
	,ENC("P")						// 암호화 관련 설정
	,CONFIDENCE("C")				// 신뢰도 값 설정
//	,OPERATE("O")					// 세션 설정
	;
	
	private final String codeKey;
}

/**
 * 
 */
package com.kt.smp.stt.error.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SttErrorEndpoint {
	TRAIN("train", "학습"),
	VERIFY("verify", "검증"),
	DEPLOY("deploy", "배포"),
	CONFIDENCE("confidence", "신뢰도"),
	DOWNLOAD("download", "다운로드"),
	SESSION("session", "세션"),
	STATISTICS("statistics", "통계"),
	SYSTEM_STATUS("systemStatus", "HW 리소스"),
	TEST("test", "테스트");

	private final String endpointEng;
	private final String endpointKor;
}

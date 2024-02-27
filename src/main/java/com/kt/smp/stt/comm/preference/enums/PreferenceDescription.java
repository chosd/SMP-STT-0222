/**
 * 
 */
package com.kt.smp.stt.comm.preference.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PreferenceDescription {
	
	SCHEDULER_USEYN("SCHEDULER_USEYN", "스케줄러 사용여부")
	, SCHEDULER_CYCLE_STATISTICS("SCHEDULER_CYCLE_STATISTICS", "통계 스케줄러 주기")
	, SCHEDULER_CYCLE_HW_RESOURCE("SCHEDULER_CYCLE_HW_RESOURCE", "HW 리소스 스케줄러 주기")
	, REMOVER_STANDARD("REMOVER_STANDARD", "삭제 기준일")
	, REMOVER_TIME("REMOVER_TIME", "삭제 실행 시간")
	, MEMORY_PERCENT("THRESHOLD_MEMORY", "메모리 사용률 임계치")
	, CPU_PERCENT("THRESHOLD_CPU", "CPU 사용률 임계치")
	, STORAGE_PERCENT("THRESHOLD_STORAGE", "디스크 사용률 임계치")
	, SESSION_TARGET("SESSION_TARGET", "세션 조회 대상 서버")
	, MULTIPART_HOST_DEPLOY("MULTIPART_HOST_DEPLOY", "HOST IP 배포 멀티파트 여부")
	, MULTIPART_HOST_TEST("MULTIPART_HOST_TEST", "HOST IP 단건 테스트 멀티파트 여부")
	, MULTIPART_SUB_DEPLOY("MULTIPART_SUB_DEPLOY", "DEPLOY IP 배포 멀티파트 여부")
	, MULTIPART_SUB_TEST("MULTIPART_SUB_TEST", "DEPLOY ID 단건 테스트 멀티파트 여부")
	, RECORD_COUNT("SCREEN_RECORDS", "페이지 사이즈 기본값")
	, CONFIDENCE_CHART_THRESHOLD("CONFIDENCE_CHART_THRESHOLD", "신뢰도 차트 임계치")
	, TEXT_ENCRYPT("TEXT_ENCRYPT", "텍스트 데이터 암호화 여부")
	, WAV_ENCRYPT("WAV_ENCRYPT", "WAV 음원 데이터 암호화 여부")
//	, SESSION_FIRST_SORT_CONDITION("SESSION_FIRST_SORT_CONDITION", "세션 첫 번째 정렬조건")
//	, SESSION_SECOND_SORT_CONDITION("SESSION_SECOND_SORT_CONDITION", "세션 두 번째 정렬조건")
	;
	private final String description;
	private final String descriptionKor;
}

/**
 * 
 */
package com.kt.smp.stt.verify.history.dto;

import com.kt.smp.stt.verify.history.type.VerifyStatus;

import lombok.Data;

@Data
public class VerifyHistoryStatusDto {
	private Integer verifyId;		// 검증 ID 
	private String resultCode;		// 결과코드
	private String resultCodeMsg; 	// 결과코드 메시지
	private String resultMsg;		// 검증 상태조회 요청 결과
	private String serviceCode;		// serviceCode 이름
	private VerifyStatus status;	// 검증 상태 ("ready": 미검증 상태, "verifying": 검증중, "complete": 검증 완료 "fail": 검증 실패)
	private Double cer;				// cer 기반 검증 결과(인식률)
	private Double wer;				// wer 기반 검증 결과
}

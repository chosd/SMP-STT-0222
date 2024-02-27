package com.kt.smp.stt.dictation.dto;

import lombok.Data;

@Data
public class ConfidenceGetResultDto {
	
    private String serviceCode;		// 서비스 코드
    
    private Float confidence;		// 신뢰도
    
    private Integer saveFlag;		// 음원 파일 저장 유무(1: 저장, 0 :미저장) -> default : 1
    
    private Integer apiFlag;		// SMP로 API 전송 유무 (1:전송, 0: 미전송) default: 1
    
    private Integer encFlag;		// 음원 암호화 처리 유무 (1:처리, 0:미처리) -> default : 0
    
    private Integer exeFlag;		// 음원파일 저장 시작 유무 필드
    
    private Integer fileStartCount; // 파일 최대 저장 개수
    
    private Integer fileSaveCount;  // 파일 현재 저장 개수
}

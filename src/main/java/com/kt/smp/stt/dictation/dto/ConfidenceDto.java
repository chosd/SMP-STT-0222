package com.kt.smp.stt.dictation.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConfidenceDto {

    private String serviceCode;
    private Float confidence;
    private Integer fileCount;
    private Integer encFlag;		// 음원 암호화 처리 유무 (1:처리, 0:미처리) -> default : 0
    private Integer saveFlag;		// 음원 파일 저장 유무(1: 저장, 0 :미저장) -> default : 1
    private Integer apiFlag;		// SMP로 API 전송 유무 (1:전송, 0: 미전송) -> default : 1
}

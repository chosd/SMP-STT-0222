package com.kt.smp.stt.confidence.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ConfidenceSaveDto {

	// confidenceData
	private String callKey;
    private String phoneType;
    private Integer txrx;
    private Long startTime;
    private Long endTime;
    private String deviceId;
    private String callWavPath;
    
    // sttResult
    private Long sttStartTime;
    private Long sttEndTime;
    private String transcript;
    private Double confidence;
    private String sentenceWavPath;				// 발화 단위의 음성 저장 경로(파일명 포함)
    
    // words
    private String words;				// Word 단위 신뢰도 정보
}

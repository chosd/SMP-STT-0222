package com.kt.smp.stt.confidence.dto;

import java.util.List;

import lombok.Data;

@Data
public class SttResultDto {

    private Long startTime;
    private Long endTime;
    private String transcript;
    private Double confidence;
    
    // 추가
    private String sentenceWavPath;				// 발화 단위의 음성 저장 경로(파일명 포함)
    private List<WordsDto> words;				// Word 단위 신뢰도 정보
}

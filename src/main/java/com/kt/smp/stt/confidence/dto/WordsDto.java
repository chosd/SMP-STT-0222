package com.kt.smp.stt.confidence.dto;

import lombok.Getter;

@Getter
public class WordsDto {

    private Long startTime;				// STT 종료시간
    private Long endTime;				// STT 시작시간
    private String word;				// STT 결과(단어)
    private Double confidence;			// STT 결과에 대한 신뢰도(단어)
}

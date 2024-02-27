package com.kt.smp.stt.recognition.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SttRecognitionVO {

    private String serviceCode;         // 서비스 코드

    private String serviceModelName;    // 평균 인식률

    private Long serviceModelId;      // 서비스 모델 ID

    private Integer totalCount;         // 총 개수

    private List<SttRecognitionDetailVO> sttRecognitionDetailList; // 인식률 추이 상세

}

package com.kt.smp.stt.recognition.dto;

import lombok.Data;

@Data
public class SttRecognitionDetailVO {

    private float avgCer;               // 평균 인식률

    private String startAt;             // 시작시간

    private String deployId;            // 배포 이력 ID
    
    private String verifyId;            // 검증 이력 ID
    
    private Integer datasetId;          // 검증 데이터 셋 ID

    private String datasetName;         // 검증 데이터 셋 이름
    
    private String resultModelId;		// 결과모델 ID
    
    private String verifyName;
    
    private String endAt;
    
}

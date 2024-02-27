package com.kt.smp.stt.confidence.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * @author jaime
 * @title SttStatisticVO
 * @see\n <pre>
 * </pre>
 * @since 2022-07-04
 */
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SttConfidenceVO{
	
	// 시작시간
	private String startAt;
	
    // 서비스 코드명
    private String serviceCode;
    
    // 서비스 모델명
    private String serviceModelName;
    
    // 서비스 모델 아이디
    private String serviceModelId;
    
    // 평균 신뢰도 값
    private float avgCer;
    
    // 개수
    private Integer totalCount;
}

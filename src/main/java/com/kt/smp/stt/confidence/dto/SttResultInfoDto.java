package com.kt.smp.stt.confidence.dto;

import lombok.*;

/**
 * @author jaime
 * @title SttStatisticVO
 * @see\n <pre>
 * </pre>
 * @since 2022-07-04
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SttResultInfoDto{
	
    // 키값
    private Integer sttId;
    
    // 값
    private Integer callInfoId;
    
    // 설명
    private String speakerType;
    
    private Integer sttSeq;
    
    private Integer startTimeStamp;
    
    private Integer endTimeStamp;
    
    private String sttJson;
    
    private String sttText;
    
    private String confidence;
    
    private String startTime;
    
    private String endTime;
    
    private String callKey;
    
}

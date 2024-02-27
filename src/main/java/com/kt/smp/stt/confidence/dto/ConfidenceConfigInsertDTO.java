package com.kt.smp.stt.confidence.dto;

import com.kt.smp.common.domain.BaseModel;

import lombok.*;

/**
 * @author jaime
 * @title SttStatisticVO
 * @see\n <pre>
 * </pre>
 * @since 2022-07-04
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConfidenceConfigInsertDTO extends BaseModel{
	
    // 키값
    private String codeKey;
    
    // 값
    private String codeValue;
    
    // 설명
    private String description;
    
}

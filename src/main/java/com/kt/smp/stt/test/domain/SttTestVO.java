package com.kt.smp.stt.test.domain;

import com.kt.smp.stt.common.ServiceModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author jaime
 * @title SttTestVO
 * @see\n <pre>
 * </pre>
 * @since 2022-03-23
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SttTestVO {

    private Long serviceModelId;
    
    private String serviceCode;

    private String uuid;
    
    private String testTarget;
}

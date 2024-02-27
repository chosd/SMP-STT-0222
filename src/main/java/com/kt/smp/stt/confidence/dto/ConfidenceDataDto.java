package com.kt.smp.stt.confidence.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ConfidenceDataDto {

    private String callkey;
    private String phoneType;
    private String serviceCode;
    private String deviceId;
    private Integer txrx;
    private SttResultDto sttResult;
}

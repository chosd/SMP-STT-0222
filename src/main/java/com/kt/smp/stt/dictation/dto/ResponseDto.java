package com.kt.smp.stt.dictation.dto;

import lombok.Data;

@Data
public class ResponseDto {

    private String resultCode;
    private String resultMsg;
    
    private ConfidenceGetResultDto confidenceInfo;
}

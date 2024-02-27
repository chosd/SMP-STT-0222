package com.kt.smp.stt.recognition.dto;

import com.kt.smp.stt.confidence.domain.SttConfidenceSearchUnit;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SttRecognitionSearchCondition {
    private List<String> serviceCodeList;

    private String serviceCode;
    
    private String from;

    private String to;
    
}

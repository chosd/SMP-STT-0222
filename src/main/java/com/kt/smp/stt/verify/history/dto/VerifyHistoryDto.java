package com.kt.smp.stt.verify.history.dto;

import lombok.Getter;

@Getter
public class VerifyHistoryDto {

    private Integer id;
    private String serviceModelName;
    private String deployedModelName;
    private String deployModelId;
    private String datasetName;
    private String description;
    private String regId;
    private String startAt;
    private String endAt;
    private String totalTime;
    private String status;
    private String failCause;
    private Double cer;
    private Double wer;
}

package com.kt.smp.stt.verify.history.dto;

import lombok.Getter;

@Getter
public class VerifyHistoryListDto {

    private Integer id;
    private String serviceModelId;
    private String serviceModelName;
    private Integer deployModelId;
    private String deployedModelName;
    private String datasetId;
    private String datasetName;
    private Integer numOfData;
    private Double cer;
    private String description;
    private String regId;
    private String regDt;
    private String status;
    private String saveYn;
}

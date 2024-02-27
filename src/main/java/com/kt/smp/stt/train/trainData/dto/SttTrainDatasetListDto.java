package com.kt.smp.stt.train.trainData.dto;

import lombok.Getter;

@Getter
public class SttTrainDatasetListDto {

    
    private Integer id;
    private Integer serviceModelId;
    private String serviceModelName;
    private String name;
    private Integer numOfData;
    private String description;
    private String regId;
    private String regDt;
}

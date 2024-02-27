package com.kt.smp.stt.verify.dataset.dto;

import lombok.Getter;

@Getter
public class VerifyDatasetListDto {

    private Integer id;
    private Integer serviceModelId;
    private String serviceModelName;
    private String name;
    private Integer numOfData;
    private String description;
    private String regId;
    private String regDt;
}

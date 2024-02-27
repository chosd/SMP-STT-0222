package com.kt.smp.stt.verify.history.dto;

import lombok.Getter;

@Getter
public class DeployedModelDto {

    private Long id;
    private String name;
    private Long serviceModelId;

    public DeployedModelDto(Long id, String name, Long serviceModelId) {
        this.id = id;
        this.name = name;
        this.serviceModelId = serviceModelId;
    }
}

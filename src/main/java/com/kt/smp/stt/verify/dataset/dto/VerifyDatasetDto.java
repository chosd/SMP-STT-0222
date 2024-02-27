package com.kt.smp.stt.verify.dataset.dto;

import lombok.Data;
import lombok.Getter;

import java.nio.file.Paths;

@Data
public class VerifyDatasetDto {

    private Integer id;
    private Integer serviceModelId;
    private String serviceModelName;
    private Integer directoryId;
    private String directoryName;
    private String directoryHomePath;
    private String directoryPath;
    private String name;
    private String description;
    private String regId;

    public String getDirectoryPath() {
        return Paths.get(directoryHomePath, directoryPath).toString();
    }
}

package com.kt.smp.stt.dictation.dto;

import lombok.Getter;

import java.nio.file.Paths;
import java.util.List;

@Getter
public class BulkUsageSaveDto {

    private Integer amTrainDataDirectoryId;
    private List<Integer> verifyDatasetIdList;
    private List<Integer> amDatasetIdList;
    private String modelType;
    private String datasetName;
    private Integer serviceModelId;
    private String amDataPath;
    private String basePath;
    private String detailPath;
    private String description;

    private List<Integer> dictationIdList;

    public void setAmDataPath(String directoryHome){
        this.amDataPath = Paths.get(directoryHome, basePath, detailPath, serviceModelId.toString()).toString() + "/";
    }

}

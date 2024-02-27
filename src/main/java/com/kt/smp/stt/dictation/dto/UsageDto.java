package com.kt.smp.stt.dictation.dto;

import com.kt.smp.stt.dictation.type.UsageType;
import lombok.Getter;

@Getter
public class UsageDto {

    private Integer id;
    private UsageType type;
    private Integer dictationId;
    private Integer verifyDatasetId;
    private Integer amDatasetId;
    private String amDataPath;
    private Integer amTrainDataDirectoryId;
    private String wavFileName;

}

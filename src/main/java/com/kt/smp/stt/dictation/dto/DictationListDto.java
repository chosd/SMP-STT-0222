package com.kt.smp.stt.dictation.dto;

import com.kt.smp.stt.dictation.type.UsageType;
import lombok.Getter;
import lombok.Setter;

import java.nio.file.Paths;

@Getter
@Setter
public class DictationListDto {

    private Integer id;
    private String wavFileName;
    private String serviceModelName;
    private String transcript;
    private String dictatedText;
    private String usageType;
    private String regId;
    private String regDt;
    private String updId;
    private String isPreempted;

    public void setWavFilePath(String wavFilePath) {
        this.wavFileName = Paths.get(wavFilePath).getFileName().toString();
    }

    public void setUsageType(UsageType usageType) {
        this.usageType = usageType.getLabel();
    }

    public void setIsPreempted(String isPreempted) {
        if ("Y".equals(isPreempted)) {
            this.isPreempted = "수정중";
            return;
        }

        this.isPreempted = "";
    }
}

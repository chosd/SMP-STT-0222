package com.kt.smp.stt.dictation.dto;

import com.kt.smp.stt.dictation.type.UsageType;
import com.kt.smp.stt.log.type.CallDirection;
import lombok.Getter;
import lombok.Setter;

import java.nio.file.Paths;
import java.util.List;

@Getter
@Setter
public class DictationDto {

    private Integer id;
    private String wavFilePath;
    private String wavFileName;
    private String serviceModelName;
    private CallDirection direction;
    private UsageType usageType;
    private String transcript;
    private String dictatedText;
    private String regId;
    private String updId;
    private Integer amTrainDataDirectoryId;
    private String amDataPath;
    private List<Integer> verifyDatasetIdList;
    private List<Integer> amDatasetIdList;
    private Integer prevId;
    private Integer nextId;
    public void setWavFilePath(String wavFilePath) {
        this.wavFilePath = wavFilePath;
        this.wavFileName = Paths.get(wavFilePath).getFileName().toString();
    }

    public void setVerifyDatasetIdList(List<Integer> verifyDatasetIdList) {
        this.verifyDatasetIdList = verifyDatasetIdList;
    }
    
    public void setTrainDatasetIdList(List<Integer> amDatasetIdList) {
        this.amDatasetIdList = amDatasetIdList;
    }

    public void setAmTrainDataDirectoryId(Integer amTrainDataDirectoryId) {
        this.amTrainDataDirectoryId = amTrainDataDirectoryId;
    }

    public void setAmDataPath(String amDataPath){
        this.amDataPath = amDataPath;
    }

    public boolean isEqual(DictationDto target) {
        return id.equals(target.getId());
    }

    public void setPrev(DictationDto prev) {

        if (prev != null) {
            prevId = prev.getId();
        }
    }

    public void setNext(DictationDto next) {

        if (next != null) {
            nextId = next.getId();
        }
    }
}

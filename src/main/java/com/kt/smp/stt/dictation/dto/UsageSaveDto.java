package com.kt.smp.stt.dictation.dto;

import com.kt.smp.stt.dictation.type.UsageType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsageSaveDto {

    private Integer dictationId;
    private Integer verifyDatasetId;
    private Integer amDatasetId;
    private Integer amTrainDataDirectoryId;
    private String amDataPath;
    private String wavFileName;
    private UsageType type;
    private String regId;
    private String regIp;
    private String updId;
    private String updIp;

    public static UsageSaveDto makeAmTrainDataUsage(DictationDto dictation, Integer amTrainDataDirectoryId) {

        UsageSaveDto usageSave = new UsageSaveDto();
        usageSave.setDictationId(dictation.getId());
        usageSave.setAmTrainDataDirectoryId(amTrainDataDirectoryId);
        usageSave.setType(UsageType.AM_TRAIN_DATA);
        return usageSave;
    }

    public static UsageSaveDto makeVerifyDataUsageSave(Integer dictationId, Integer verifyDatasetId) {

        UsageSaveDto usageSave = new UsageSaveDto();
        usageSave.setDictationId(dictationId);
        usageSave.setVerifyDatasetId(verifyDatasetId);
        usageSave.setType(UsageType.VERIFY_DATA);
        return usageSave;
    }

    public static UsageSaveDto makeAmTrainDataUsageSave(Integer dictationId, Integer amTrainDataDirectoryId) {

        UsageSaveDto usageSave = new UsageSaveDto();
        usageSave.setDictationId(dictationId);
        usageSave.setAmTrainDataDirectoryId(amTrainDataDirectoryId);
        usageSave.setType(UsageType.AM_TRAIN_DATA);
        return usageSave;
    }

    public static UsageSaveDto makeAmTrainDataUsageSave(Integer dictationId, String amDataPath){
        UsageSaveDto usageSave = new UsageSaveDto();
        usageSave.setDictationId(dictationId);
        usageSave.setAmDataPath(amDataPath);
        usageSave.setType(UsageType.AM_TRAIN_DATA);
        return usageSave;
    }

    public void audit(DictationUpdateDto modifiedDictation) {

        this.regId = modifiedDictation.getUpdId();
        this.regIp = modifiedDictation.getUpdIp();
        this.updId = modifiedDictation.getUpdId();
        this.updIp = modifiedDictation.getUpdIp();

    }

    public boolean isSameAmTrainDataUsage(UsageDto oldUsage) {
        return amTrainDataDirectoryId.equals(oldUsage.getAmTrainDataDirectoryId());
    }

    public boolean isSameVerifyDataUsageWith(UsageDto oldUsage) {
        return verifyDatasetId.equals(oldUsage.getVerifyDatasetId());
    }
    
    public boolean isSameAmDataUsageWith(UsageDto oldUsage) {
        return amDatasetId.equals(oldUsage.getAmDatasetId());
    }

    public void setWavFileName(String wavFileName) {
        this.wavFileName = wavFileName;
    }
}

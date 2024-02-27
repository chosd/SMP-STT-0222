package com.kt.smp.stt.verify.data.dto;


import javax.servlet.http.HttpServletRequest;

import com.kt.smp.multitenancy.dto.MasterSmpRequestHeaderDto;
import com.kt.smp.stt.dictation.dto.DictationDto;
import com.kt.smp.stt.dictation.dto.DictationToVerifyDataDto;
import com.kt.smp.stt.dictation.dto.UsageSaveDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VerifyDataSaveDto {

    private Integer datasetId;
    private Integer serviceModelId;
    private String datasetName;
    private String basePath;
    private String detailPath;
    private String verifyDataPath;
    private String wavFileName;
    private String wavFilePath;
    private String answerFileName;
    private String dictatedText;
    private String description;
    private String regId;
    private String regIp;
    private String updId;
    private String updIp;

    public VerifyDataSaveDto(VerifyDataMultipartSaveDto newData, AnswerSheetDto answerSheet) {

        this.datasetId = newData.getDatasetId();
        this.serviceModelId = newData.getServiceModelId();
        this.datasetName = newData.getDatasetName();
        this.basePath = newData.getBasePath();
        this.detailPath = newData.getDetailPath();
        this.verifyDataPath = newData.getVerifyDataPath();
        this.wavFileName = answerSheet.getWavFileName();
        this.answerFileName = newData.getAnswerSheet().getOriginalFilename();
        this.dictatedText = answerSheet.getDictatedText();
        this.description = newData.getDescription();
        this.regId = newData.getRegId();
        this.regIp = newData.getRegIp();
        this.updId = newData.getUpdId();
        this.updIp = newData.getUpdIp();
    }
    
    public VerifyDataSaveDto(DictationToVerifyDataDto newData, DictationDto dictation) {

        this.datasetId = newData.getDatasetId();
        this.serviceModelId = newData.getServiceModelId();
        this.datasetName = newData.getDatasetName();
        this.basePath = newData.getBasePath();
        this.detailPath = newData.getDetailPath();
        this.verifyDataPath = newData.getVerifyDataPath();
        this.description = newData.getDescription();
        this.regId = newData.getRegId();
        this.regIp = newData.getRegIp();
        this.updId = newData.getUpdId();
        this.updIp = newData.getUpdIp();
        
        this.wavFileName = dictation.getWavFileName();
        this.wavFilePath = dictation.getWavFilePath();
        this.dictatedText = dictation.getDictatedText();
    }

    public VerifyDataSaveDto(DictationDto dictation, UsageSaveDto newUsage) {

        this.wavFileName = dictation.getWavFileName();
        this.dictatedText = dictation.getDictatedText();
        this.datasetId = newUsage.getVerifyDatasetId();
        this.regId = newUsage.getRegId();
        this.regIp = newUsage.getRegIp();
        this.updId = newUsage.getUpdId();
        this.updIp = newUsage.getUpdIp();

    }
    
    public VerifyDataSaveDto(DictationDto dictation, Integer datasetId, HttpServletRequest request) {
    	
        MasterSmpRequestHeaderDto header = MasterSmpRequestHeaderDto.generate(request);
        this.regId = header.getUserId();
        this.regIp = request.getRemoteAddr();
        this.updId = header.getUserId();
        this.updIp = request.getRemoteAddr();
        
        this.wavFileName = dictation.getWavFileName();
        this.dictatedText = dictation.getDictatedText();
        this.datasetId = datasetId;

    }
    
}

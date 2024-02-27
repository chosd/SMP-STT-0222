package com.kt.smp.stt.verify.data.dto;

import lombok.Getter;

@Getter
public class VerifyDataListDto {

    private Integer id;
    private String serviceModelName;
    private String datasetName;
    private Integer datasetId;
    private Integer serviceModelId;
    private String wavFileName;
    private String dictatedText;
    private String regId;
    private String regDt;
    
    public void setDictatedText(String dictatedText) {
    	this.dictatedText = dictatedText;
    }
}

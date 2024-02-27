package com.kt.smp.stt.verify.data.dto;

import lombok.Getter;

@Getter
public class VerifyDataDto {

    private Integer id;
    private String serviceModelName;
    private Integer datasetId;
    private String basePath;
    private String detailPath;
    private String verifyDataPath;
    private String datasetName;
    private String description;
    private String wavFileName;
    private String answerFileName;
    private String dictatedText;
    
    public void setDictatedText(String dictatedText) {
    	this.dictatedText = dictatedText;
    }
    
}

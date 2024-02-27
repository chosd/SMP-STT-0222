package com.kt.smp.stt.verify.data.dto;

import lombok.Getter;

@Getter
public class AnswerSheetDto {

    private String wavFileName;
    private String dictatedText;

    public AnswerSheetDto(String wavFileName, String dictatedText) {
        this.wavFileName = wavFileName;
        this.dictatedText = dictatedText;
    }
}

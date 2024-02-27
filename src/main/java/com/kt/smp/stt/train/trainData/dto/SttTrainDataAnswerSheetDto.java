package com.kt.smp.stt.train.trainData.dto;

import lombok.Getter;

@Getter
public class SttTrainDataAnswerSheetDto {

    private String wavFileName;
    private String answerText;

    public SttTrainDataAnswerSheetDto(String wavFileName, String answerText) {
        this.wavFileName = wavFileName;
        this.answerText = answerText;
    }
	
}

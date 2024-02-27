package com.kt.smp.stt.train.trainData.dto;

import com.kt.smp.stt.verify.data.dto.AnswerSheetDto;
import com.kt.smp.stt.verify.data.dto.VerifyDataMultipartSaveDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SttTrainDataAmSaveDto {

	private Integer amDataId;
    private String wavFileName;
    private String answerText;
    private String regId;
    private String regIp;
    private String updId;
    private String updIp;
    
    public SttTrainDataAmSaveDto(SttTrainDataMultipartSaveDto newData, SttTrainDataAnswerSheetDto answerSheet) {

        this.amDataId = newData.getAmDataId();
        this.wavFileName = answerSheet.getWavFileName();
        this.answerText = answerSheet.getAnswerText();
        this.regId = newData.getRegId();
        this.regIp = newData.getRegIp();
        this.updId = newData.getUpdId();
        this.updIp = newData.getUpdIp();
    }
	
}

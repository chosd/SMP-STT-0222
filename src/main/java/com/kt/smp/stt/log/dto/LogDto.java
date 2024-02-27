package com.kt.smp.stt.log.dto;

import com.kt.smp.stt.log.type.CallDirection;
import com.kt.smp.stt.log.type.CallStatus;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LogDto {

    private Integer id;
    private String callKey;
    private CallDirection direction;
    private String startAt;
    private String endAt;
    private String wavFilePath;
    private String sentenceWavFilePath;
    private Long serviceModelId;
    private String serviceModelName;
    private String assistantId;
    private CallStatus status;
    private String transcript;
    private Integer transcriptStart;
    private Integer transcriptEnd;
    private Double cer;
    private String usedAsDictation;
    private Integer dictationId;
    private String dictationUseYn;

    public boolean isUsedAsDictation() {
        return usedAsDictation.equals("Y");
    }

    public void setDictationId(Integer dictationId) {
        this.dictationId = dictationId;
    }
}

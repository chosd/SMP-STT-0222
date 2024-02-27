package com.kt.smp.stt.verify.history.dto;

import com.kt.smp.stt.verify.history.type.VerifyStatus;
import lombok.Getter;

@Getter
public class VerifyHistoryUpdateDto {

    private Integer id;
    private VerifyStatus status;
    private Double cer;
    private Double wer;
    private String failCause;

    public VerifyHistoryUpdateDto(Integer id, VerifyStatus status, Double cer, Double wer, String failCause) {
        this.id = id;
        this.status = status;
        this.cer = cer;
        this.wer = wer;
        this.failCause = failCause;
    }
}

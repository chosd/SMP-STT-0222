package com.kt.smp.stt.verify.request.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class EngineVerifyRequestDto {
    private String serviceCode;
    private String verificationWavPath;
    private String correctAnswerPath;
    private String callbackUrl;

    public EngineVerifyRequestDto(String serviceCode, String verificationWavPath, String correctAnswerPath, String callbackUrl) {
        this.serviceCode = serviceCode;
        this.verificationWavPath = verificationWavPath;
        this.correctAnswerPath = correctAnswerPath;
        this.callbackUrl = callbackUrl;
    }
}

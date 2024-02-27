package com.kt.smp.stt.verify.request.dto;

import lombok.Getter;

@Getter
public class VerifyStatusResponseDto {

    private String resultCode;
    private String resultMsg;
    private String serviceCode;
    private String status;
    private Double cer;
    private Double wer;

}

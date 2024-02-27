package com.kt.smp.stt.verify.request.dto;

import lombok.Data;

@Data
public class VerifyCallbackDto {

    private String resultCode;
    private String resultMsg;
    private String serviceCode;
    private String status;
    private double cer;
    private double wer;

}

package com.kt.smp.stt.verify.request.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VerifyResponseDto {

    private String resultCode;
    private String resultMsg;

    public VerifyResponseDto(String resultCode, String resultMsg) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }

    public static VerifyResponseDto success() {
        return new VerifyResponseDto("0000", "success");
    }

    public static VerifyResponseDto fail(String cause) {
        return new VerifyResponseDto("9999", cause);
    }
}


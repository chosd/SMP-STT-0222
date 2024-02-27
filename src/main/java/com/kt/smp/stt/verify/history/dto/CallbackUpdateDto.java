package com.kt.smp.stt.verify.history.dto;

import com.kt.smp.stt.verify.history.type.VerifyStatus;

import lombok.Data;

@Data
public class CallbackUpdateDto {

    private Long id;
    VerifyStatus status;
}

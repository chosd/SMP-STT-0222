package com.kt.smp.stt.verify.data.dto;

import lombok.Getter;

@Getter
public class VerifyDataUpdateDto {

    private Integer id;
    private String dictatedText;
    private String updId;
    private String updIp;

    public VerifyDataUpdateDto(Integer id, String dictatedText, String updId, String updIp) {
        this.id = id;
        this.dictatedText = dictatedText;
        this.updId = updId;
        this.updIp = updIp;
    }
}

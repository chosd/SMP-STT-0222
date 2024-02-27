package com.kt.smp.stt.log.dto;

import com.kt.smp.stt.log.type.CallStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogListDto {

    private String callKey;
    private String status;
    private String serviceModelName;
    private Integer numOfTranscript;
    private String worstTranscript;
    private Double worstCer;
    private String startAt;

    public void setStatus(CallStatus status) {
        this.status = status.getLabel();
    }
}

package com.kt.smp.stt.log.dto;

import com.kt.smp.stt.log.type.CallDirection;
import com.kt.smp.stt.log.type.CallStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
public class LogDetailDto {

    private final String callKey;
    private final String status;
    private final String serviceModelName;
    private final CallDirection direction;
    private final String startAt;
    private final List<LogDto> logList;

    public LogDetailDto(String callKey, CallStatus status, String serviceModelName, CallDirection direction, String startAt, List<LogDto> logList) {
        this.callKey = callKey;
        this.status = status.getLabel();
        this.serviceModelName = serviceModelName;
        this.direction = direction;
        this.startAt = startAt;
        this.logList = logList;
    }
}

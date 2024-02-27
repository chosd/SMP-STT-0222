package com.kt.smp.stt.log.dto;

import com.kt.smp.stt.log.type.CallStatus;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
public class LogSearchCondition {

    private String callKey;
    private CallStatus status;
    private Long serviceModelId;
    private String transcript;
    private Double cerFrom;
    private Double cerTo;
    private String startAtFrom;
    private String startAtTo;
    private Integer start;
    private Integer size;

    public void setStartAtFrom(String startAtFrom) {
        if (StringUtils.isBlank(startAtFrom)) {
            this.startAtFrom = null;
            return;
        }

        this.startAtFrom = startAtFrom.split(" ")[0];
    }

    public void setStartAtTo(String startAtTo) {
        if (StringUtils.isBlank(startAtTo)) {
            this.startAtTo = null;
            return;
        }

        this.startAtTo = startAtTo.split(" ")[0];
    }
    public void setStartTimeAtTo(String startAtTo) {
        this.startAtTo = startAtTo;
    }
}

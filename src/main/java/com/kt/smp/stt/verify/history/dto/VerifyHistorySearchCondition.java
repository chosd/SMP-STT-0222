package com.kt.smp.stt.verify.history.dto;

import com.kt.smp.stt.verify.history.type.VerifyStatus;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
public class VerifyHistorySearchCondition {

    private Integer serviceModelId;
    private VerifyStatus status;
    private String datasetName;
    private String description;
    private Integer cerFrom;
    private Integer cerTo;
    private String regId;
    private String regDtFrom;
    private String regDtTo;
    private Integer start;
    private Integer size;

    public void setRegDtFrom(String regDtFrom) {
        if (StringUtils.isBlank(regDtFrom)) {
            this.regDtFrom = null;
            return;
        }

        this.regDtFrom = regDtFrom.split(" ")[0];
    }

    public void setRegDtTo(String regDtTo) {
        if (StringUtils.isBlank(regDtTo)) {
            this.regDtTo = null;
            return;
        }

        this.regDtTo = regDtTo.split(" ")[0];
    }
}

package com.kt.smp.stt.verify.data.dto;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
public class VerifyDataSearchCondition {

    private Integer serviceModelId;
    private String regDtFrom;
    private String regDtTo;
    private String datasetName;
    private String dictatedText;
    private String regId;
    private String description;
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

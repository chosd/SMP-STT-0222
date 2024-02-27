package com.kt.smp.stt.dictation.dto;

import com.kt.smp.stt.dictation.type.UsageType;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
public class DictationSearchCondition {

    private Integer serviceModelId;
    private UsageType usageType;
    private String wavFileName;
    private String dictatedText;
    private String updId;
    private String regDtFrom;
    private String regDtTo;
    private Integer size;
    private Integer start;
    private Integer originSize;
    private Integer originStart;


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

    public void extendSearchRange() {

        originSize = size;
        originStart = start;

        size += 2;
        if (start > 0) {
            start--;
        }
    }
}

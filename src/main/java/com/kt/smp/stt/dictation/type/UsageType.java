package com.kt.smp.stt.dictation.type;

import lombok.Getter;

@Getter
public enum UsageType {

    NONE(""),
    VERIFY_DATA("검증데이터"),
    AM_TRAIN_DATA("AM 학습데이터");

    private final String label;

    UsageType(String label) {
        this.label = label;
    }
}

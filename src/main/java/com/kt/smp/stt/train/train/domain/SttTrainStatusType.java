package com.kt.smp.stt.train.train.domain;

import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum SttTrainStatusType {

    READY("READY", "미학습 상태"),
    TRAINING("TRAINING", "학습 중"),
    COMPLETE("COMPLETE", "학습 완료"),
    FAIL("FAIL", "학습 실패");

    private static final Map<String, SttTrainStatusType> map = new HashMap<>(values().length, 1);

    static {
        for (SttTrainStatusType type : values()) {
            map.put(type.code, type);
        }
    }

    private final String code;

    private final String label;

    SttTrainStatusType(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public static SttTrainStatusType of(String code) {
        if (StringUtils.isEmpty(code)) {
            return null;
        }

        SttTrainStatusType type = map.get(code);

        if (ObjectUtils.isEmpty(code)) {
            throw new IllegalArgumentException("Invalid SttTrainStatusType code: " + code);
        }

        return type;
    }
}

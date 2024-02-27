package com.kt.smp.stt.deploy.deploy.domain;

import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum SttDeployStatusType {

    READY("READY", "미배포 상태"),
    DEPLOYING("DEPLOYING", "배포 중"),
    COMPLETE("COMPLETE", "배포 완료"),
    FAIL("FAIL", "실패");

    private static final Map<String, SttDeployStatusType> map = new HashMap<>(values().length, 1);

    static {
        for (SttDeployStatusType type : values()) {
            map.put(type.code, type);
        }
    }

    private final String code;

    private final String label;

    SttDeployStatusType(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public static SttDeployStatusType of(String code) {
        if (StringUtils.isEmpty(code)) {
            return null;
        }

        SttDeployStatusType type = map.get(code);

        if (ObjectUtils.isEmpty(type)) {
            throw new IllegalArgumentException("Invalid SttDeployStatusType code: " + code);
        }

        return type;
    }
}

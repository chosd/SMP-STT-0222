package com.kt.smp.stt.verify.history.type;

import lombok.Getter;

@Getter
public enum VerifyStatus {

    READY("READY", "준비중"),
    VERIFYING("VERIFYING", "검증중"),
    COMPLETE("COMPLETE" ,"완료"),
    FAIL("FAIL", "실패");

    private final String code;
    private final String label;

    VerifyStatus(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public static VerifyStatus findByCode(String code) {
        String upperCaseCode = code.toUpperCase();

        if ("READY".equals(upperCaseCode)) {
            return READY;
        }

        if ("VERIFYING".equals(upperCaseCode)) {
            return VERIFYING;
        }

        if ("COMPLETE".equals(upperCaseCode)) {
            return COMPLETE;
        }

        if ("FAIL".equals(upperCaseCode)) {
            return FAIL;
        }

        throw new IllegalArgumentException("알 수 없는 검증상태");
    }
}

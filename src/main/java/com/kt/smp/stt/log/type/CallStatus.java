package com.kt.smp.stt.log.type;

import lombok.Getter;

@Getter
public enum CallStatus {

    COMPLETE("완결"),
    INCOMPLETE("미완결");

    private final String label;

    CallStatus(String label) {
        this.label = label;
    }
}

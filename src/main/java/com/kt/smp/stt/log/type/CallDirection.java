package com.kt.smp.stt.log.type;

public enum CallDirection {
    TX("0"),
    RX("1"),
	TXRX("2");

    private final String code;

    CallDirection(String code) {
        this.code = code;
    }
}

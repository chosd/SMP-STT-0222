package com.kt.smp.stt.common.cipher.binary;

import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.StandardCharsets;

public class InitialVector {

    private final String vector;

    public InitialVector(String vector) {
        assertVector(vector);
        this.vector = vector;
    }

    private void assertVector(String vector) {
        if (vector == null || vector.length() != 16) {
            throw new IllegalArgumentException("IV의 길이가 16이 아닙니다");
        }
    }

    public IvParameterSpec getSpec() {
        return new IvParameterSpec(vector.getBytes(StandardCharsets.UTF_8));
    }
}

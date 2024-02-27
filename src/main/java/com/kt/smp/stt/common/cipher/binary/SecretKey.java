package com.kt.smp.stt.common.cipher.binary;

import javax.crypto.spec.SecretKeySpec;

public class SecretKey {

    private final String key;

    public SecretKey(String key) {
        assertKey(key);
        this.key = key;
    }

    private void assertKey(String key) {
        if (key == null || key.length() != 32) {
            throw new IllegalArgumentException("키의 길이가 32가 아닙니다");
        }
    }

    public SecretKeySpec getSpec(Transformation transformation) {
        return new SecretKeySpec(key.getBytes(), transformation.getAlgorithm());
    }
}

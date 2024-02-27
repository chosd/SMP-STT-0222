package com.kt.smp.stt.common.cipher.db;

public interface Cipherable {

    void encode(CipherWrapper cipherWrapper);

    void decode(CipherWrapper cipherWrapper);
}

package com.kt.smp.stt.common.cipher.binary;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EncryptedFileReaderBuilder {

    @Value("${cipher.secretKey}")
    private String key;

    @Value("${cipher.iv}")
    private String iv;

    public EncryptedFileReader build() {
        FileHeader fileHeader = new FileHeader(
                "KTCC",
                new ByteRange(0, 4),
                new ByteRange(4, 8));

        BlockHeader blockHeader = new BlockHeader(
                new ByteRange(0, 2),
                new ByteRange(2, 4));

        BinaryCipher binaryCipher = makeCipher();
        return new EncryptedFileReader(fileHeader, blockHeader, binaryCipher);

    }

    private BinaryCipher makeCipher() {
        SecretKey secretKey = new SecretKey(key);
        InitialVector initialVector = new InitialVector(iv);
        Transformation transformation = new Transformation(CipherAlgorithm.AES, BlockType.CBC, PaddingMode.NoPadding);
        return new BinaryCipher(transformation, secretKey, initialVector);
    }
}

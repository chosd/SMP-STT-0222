package com.kt.smp.stt.common.cipher.binary;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class BinaryCipher {

    private final Transformation transformation;
    private final SecretKey secretKey;
    private final InitialVector iv;
    private final Cipher cipher;

    public BinaryCipher(Transformation transformation, SecretKey secretKey, InitialVector iv) {

        try {
            this.transformation = transformation;
            this.secretKey = secretKey;
            this.iv = iv;
            this.cipher = Cipher.getInstance(transformation.transfer());
        } catch (NoSuchPaddingException ex) {
            throw new IllegalArgumentException("알 수 없는 패딩모드");
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalArgumentException("알 수 없는 알고리즘");

        }
    }

    public byte[] decrypt(byte[] input) {

        try {
            cipher.init(Cipher.DECRYPT_MODE, secretKey.getSpec(transformation), iv.getSpec());
            return cipher.doFinal(input);
        } catch (InvalidAlgorithmParameterException ex) {
            throw new IllegalArgumentException("잘못된 알고리즘 설정");

        } catch (InvalidKeyException ex) {
            throw new IllegalArgumentException("잘못된 키 설정");

        } catch (IllegalBlockSizeException ex) {
            throw new IllegalArgumentException("잘못된 블록크기 설정");

        } catch (BadPaddingException ex) {
            throw new IllegalArgumentException("잘못된 패딩 설정");
        }
    }
}

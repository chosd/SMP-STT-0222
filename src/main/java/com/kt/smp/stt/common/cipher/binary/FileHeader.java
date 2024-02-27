package com.kt.smp.stt.common.cipher.binary;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class FileHeader {

    private final String id;
    private final ByteRange idRange;
    private final ByteRange wavSizeRange;

    public FileHeader(String id, ByteRange idRange, ByteRange wavSizeRange) {
        this.id = id;
        this.idRange = idRange;
        this.wavSizeRange = wavSizeRange;
    }

    public String extractId(byte[] source) {

        byte[] id = new byte[idRange.length()];
        System.arraycopy(source, idRange.from(), id, 0, idRange.length());
        return new String(id);
    }

    public boolean isNotEncrypted(String id) {
        return !this.id.equals(id);
    }

    public byte[] removeHeader(byte[] source) {
        int bufferSize = source.length - totalSize();
        byte[] buffer = new byte[bufferSize];
        System.arraycopy(source, totalSize(), buffer, 0, bufferSize);
        return buffer;
    }

    private int totalSize() {
        return idRange.length() + wavSizeRange.length();
    }

    public int extractFileSize(byte[] source) {
        byte[] wavFile = new byte[wavSizeRange.length()];
        System.arraycopy(source, wavSizeRange.from(), wavFile, 0, wavSizeRange.length());
        ByteBuffer buffer = ByteBuffer.wrap(wavFile);
        buffer.order(ByteOrder.nativeOrder());
        return buffer.getInt();
    }
}

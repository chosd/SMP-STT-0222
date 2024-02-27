package com.kt.smp.stt.common.cipher.binary;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class BlockHeader {

    private final ByteRange encSizeRange;
    private final ByteRange orgSizeRange;

    public BlockHeader(ByteRange encSizeRange, ByteRange orgSizeRange) {
        this.encSizeRange = encSizeRange;
        this.orgSizeRange = orgSizeRange;
    }

    public byte[] getBlocks(int fileSize, byte[] withoutFileHeader, BinaryCipher binaryCipher) {
        byte[] buffer = new byte[fileSize + 4];

        int from = 0;
        int destPos = 0;

        while (from < fileSize) {
            byte[] block = getBlock(from, withoutFileHeader);
            byte[] decryptedBlock = binaryCipher.decrypt(block);
            System.arraycopy(decryptedBlock, 0, buffer, destPos, decryptedBlock.length);
            destPos += decryptedBlock.length;
            from += totalSize() + block.length;
        }

        return buffer;
    }

    private byte[] getBlock(int from, byte[] source) {

        int encSize = extractEncSize(from, source);
        int blockStart = from + totalSize();
        byte[] buffer = new byte[encSize];
        System.arraycopy(source, blockStart, buffer, 0, encSize);
        return buffer;
    }

    private int extractEncSize(int from, byte[] source) {
        byte[] buffer = new byte[encSizeRange.length()];
        System.arraycopy(source, from, buffer, encSizeRange.from(), encSizeRange.length());
        ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
        byteBuffer.order(ByteOrder.nativeOrder());
        return byteBuffer.getShort();
    }

    private int totalSize() {
        return encSizeRange.length() + orgSizeRange.length();
    }
}

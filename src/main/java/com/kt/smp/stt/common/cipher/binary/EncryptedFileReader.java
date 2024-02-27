package com.kt.smp.stt.common.cipher.binary;

public class EncryptedFileReader {

    private final FileHeader fileHeader;
    private final BlockHeader blockHeader;
    private final BinaryCipher binaryCipher;

    public EncryptedFileReader(FileHeader fileHeader, BlockHeader blockHeader, BinaryCipher binaryCipher) {
        this.fileHeader = fileHeader;
        this.blockHeader = blockHeader;
        this.binaryCipher = binaryCipher;
    }

    public byte[] read(byte[] source) {
        assertSource(source);

        byte[] withoutFileHeader = fileHeader.removeHeader(source);
        int fileSize = fileHeader.extractFileSize(source);
        return blockHeader.getBlocks(fileSize, withoutFileHeader, binaryCipher);
    }

    public boolean isEncrypted(byte[] source) {
        String id = fileHeader.extractId(source);
        return !fileHeader.isNotEncrypted(id);
    }

    private void assertSource(byte[] source) {

        String id =  fileHeader.extractId(source);
        if (fileHeader.isNotEncrypted(id)) {
            throw new IllegalArgumentException("복호화 할 수 없는 파일입니다");
        }
    }


}

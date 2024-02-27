package com.kt.smp.stt.common.cipher.binary;

public class Transformation {

    private final CipherAlgorithm algorithm;
    private final BlockType blockType;
    private final PaddingMode paddingMode;

    public Transformation(CipherAlgorithm algorithm, BlockType blockType, PaddingMode paddingMode) {
        assertTransformation(algorithm, blockType, paddingMode);
        this.algorithm = algorithm;
        this.blockType = blockType;
        this.paddingMode = paddingMode;
    }

    private void assertTransformation(CipherAlgorithm algorithm, BlockType blockType, PaddingMode paddingMode) {
        if (algorithm == null) {
            throw new IllegalArgumentException("알고리즘이 설정되지 않았습니다");
        }

        if (blockType == null) {
            throw new IllegalArgumentException("블록타입이 설정되지 않았습니다");
        }

        if (paddingMode == null) {
            throw new IllegalArgumentException("패딩종류가 설정되지 않았습니다");
        }
    }

    public String transfer() {
        return algorithm.name() + "/" + blockType.name() + "/" + paddingMode.name();
    }

    public String getAlgorithm() {
        return algorithm.name();
    }
}

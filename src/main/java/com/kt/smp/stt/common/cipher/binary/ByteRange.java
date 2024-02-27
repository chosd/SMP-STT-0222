package com.kt.smp.stt.common.cipher.binary;

public class ByteRange {

    private final int from;
    private final int to;

    public ByteRange(int from, int to) {
        this.from = from;
        this.to = to;
    }

    public int length() {
        return to -from;
    }

    public int from() {
        return from;
    }

    public int to() {
        return to;
    }
}

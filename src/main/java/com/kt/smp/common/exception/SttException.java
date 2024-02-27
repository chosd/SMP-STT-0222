package com.kt.smp.common.exception;

import com.kt.smp.stt.common.component.SttCmsResultStatus;

public class SttException extends RuntimeException {

    private SttCmsResultStatus status;

    public SttException(SttCmsResultStatus status) {
        super(status.getResultMessage());
        setStatus(status);
    }

    public SttException(SttCmsResultStatus status, Throwable cause) {
        super(status.getResultMessage(), cause);
        setStatus(status);
    }

    public SttCmsResultStatus getStatus() {
        return status;
    }

    protected void setStatus(SttCmsResultStatus status) {
        this.status = status;
    }
}

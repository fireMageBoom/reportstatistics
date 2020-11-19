package com.yzcloud.reportstatistics.model;

import com.sun.istack.internal.NotNull;

public abstract class AbstractResponseEntity {

    @NotNull
    private String requestId;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}

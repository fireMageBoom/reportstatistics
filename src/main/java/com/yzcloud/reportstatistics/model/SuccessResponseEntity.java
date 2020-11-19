package com.yzcloud.reportstatistics.model;

import com.yzcloud.reportstatistics.model.jwt.JWTToken;

public class SuccessResponseEntity extends AbstractResponseEntity {

    private JWTToken data;

    public JWTToken getData() {
        return data;
    }

    public void setData(JWTToken data) {
        this.data = data;
    }
}

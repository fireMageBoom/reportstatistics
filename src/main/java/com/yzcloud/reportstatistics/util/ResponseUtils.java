package com.yzcloud.reportstatistics.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yzcloud.reportstatistics.model.AbstractResponseEntity;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

public class ResponseUtils {

    public String createRequestId() {
        String uuid = UUID.randomUUID().toString();
        return uuid;
    }

    public void createResponseEntity(HttpServletResponse response, AbstractResponseEntity abstractResponseEntity) throws IOException {
        response.setContentType("application/json; charset=utf-8");
        PrintWriter writer = response.getWriter();
        abstractResponseEntity.setRequestId(this.createRequestId());
        writer.write(new ObjectMapper().writeValueAsString(abstractResponseEntity));
    }
}

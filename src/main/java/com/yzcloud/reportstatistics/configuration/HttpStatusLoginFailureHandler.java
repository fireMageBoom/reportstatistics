package com.yzcloud.reportstatistics.configuration;

import com.yzcloud.reportstatistics.model.AbstractResponseEntity;
import com.yzcloud.reportstatistics.model.FailureResponseEntity;
import com.yzcloud.reportstatistics.util.ResponseUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HttpStatusLoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        ResponseUtils responseUtils = new ResponseUtils();
        FailureResponseEntity responseEntity = new FailureResponseEntity();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        responseEntity.setCode("Unauthorized");
        responseEntity.setMessage("the username or password is error");
        responseUtils.createResponseEntity(response, responseEntity);
    }
}

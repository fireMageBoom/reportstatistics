package com.yzcloud.reportstatistics.configuration;

import com.yzcloud.reportstatistics.model.SuccessResponseEntity;
import com.yzcloud.reportstatistics.util.ResponseUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        ResponseUtils responseUtils = new ResponseUtils();
        SuccessResponseEntity responseEntity = new SuccessResponseEntity();
        response.setStatus(HttpStatus.OK.value());
        responseUtils.createResponseEntity(response, responseEntity);
    }
}

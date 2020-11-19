package com.yzcloud.reportstatistics.configuration;

import com.yzcloud.reportstatistics.model.SuccessResponseEntity;
import com.yzcloud.reportstatistics.model.jwt.JWTToken;
import com.yzcloud.reportstatistics.service.JwtUserService;
import com.yzcloud.reportstatistics.util.ResponseUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JsonLoginSuccessHandler implements AuthenticationSuccessHandler {

    private JwtUserService jwtUserService;

    public JsonLoginSuccessHandler(JwtUserService jwtUserService) {
        this.jwtUserService = jwtUserService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String token = jwtUserService.saveUserLoginInfo((UserDetails) authentication.getPrincipal());
        ResponseUtils responseUtils = new ResponseUtils();
        SuccessResponseEntity responseEntity = new SuccessResponseEntity();
        response.setHeader("Authorization", token);
        response.setStatus(HttpStatus.OK.value());
        JWTToken jwtToken = new JWTToken();
        jwtToken.setToken(token);
        responseEntity.setData(jwtToken);
        responseUtils.createResponseEntity(response, responseEntity);
    }
}

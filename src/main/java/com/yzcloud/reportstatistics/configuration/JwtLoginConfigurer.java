package com.yzcloud.reportstatistics.configuration;

import com.yzcloud.reportstatistics.security.filter.JwtAuthenticationFilter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.logout.LogoutFilter;

public class JwtLoginConfigurer<T extends AbstractHttpConfigurer<T, B>, B extends HttpSecurityBuilder<B>> extends AbstractHttpConfigurer<T, B> {

    private JwtAuthenticationFilter authFilter;

    public JwtLoginConfigurer() {
        this.authFilter = new JwtAuthenticationFilter();
    }

    @Override
    public void configure(B http) throws Exception {
        authFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        authFilter.setAuthenticationFailureHandler(new JWTAuthenticationFailureHandler());

        JwtAuthenticationFilter filter = postProcess(authFilter);
        http.addFilterBefore(filter, LogoutFilter.class);
    }
}

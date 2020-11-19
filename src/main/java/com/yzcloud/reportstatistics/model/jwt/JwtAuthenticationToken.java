package com.yzcloud.reportstatistics.model.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private UserDetails principal;

    private String credentials;

    private DecodedJWT token;

    public JwtAuthenticationToken(DecodedJWT token) {
        super(Collections.emptyList());
        this.token = token;
    }

    public JwtAuthenticationToken(Collection<? extends GrantedAuthority> authorities, UserDetails principal, DecodedJWT token) {
        super(authorities);
        this.principal = principal;
        this.token = token;
    }

    @Override
    public UserDetails getPrincipal() {
        return principal;
    }

    public void setPrincipal(UserDetails principal) {
        this.principal = principal;
    }

    @Override
    public String getCredentials() {
        return credentials;
    }

    public void setCredentials(String credentials) {
        this.credentials = credentials;
    }

    public DecodedJWT getToken() {
        return token;
    }

    public void setToken(DecodedJWT token) {
        this.token = token;
    }
}

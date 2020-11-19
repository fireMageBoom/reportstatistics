package com.yzcloud.reportstatistics.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.yzcloud.reportstatistics.model.jwt.JwtAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.NonceExpiredException;

public class JwtAuthenticationProvider implements AuthenticationProvider {

    private JwtUserService jwtUserService;

    public JwtAuthenticationProvider(JwtUserService jwtUserService) {
        this.jwtUserService = jwtUserService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        DecodedJWT jwt = ((JwtAuthenticationToken)authentication).getToken();
        String username = jwt.getSubject();
        UserDetails user = jwtUserService.getUserLoginInfo(username);
        if(user == null || user.getPassword() == null)
            throw new NonceExpiredException("Token expires");
        String encryptSalt = user.getPassword();
        try {
            Algorithm algorithm = Algorithm.HMAC256(encryptSalt);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withSubject(username)
                    .build();
            verifier.verify(jwt.getToken());
        } catch (Exception e) {
            throw new BadCredentialsException("JWT token verify fail", e);
        }
        JwtAuthenticationToken token = new JwtAuthenticationToken(user.getAuthorities(), user, jwt);
        return token;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(JwtAuthenticationToken.class);
    }
}

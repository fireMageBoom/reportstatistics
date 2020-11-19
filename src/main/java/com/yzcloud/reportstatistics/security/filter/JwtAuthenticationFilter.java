package com.yzcloud.reportstatistics.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.yzcloud.reportstatistics.model.jwt.JwtAuthenticationToken;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private RequestMatcher requiresAuthenticationRequestMatcher;
    private AuthenticationManager authenticationManager;

    private AuthenticationFailureHandler failureHandler = new SimpleUrlAuthenticationFailureHandler();
    private AuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
//    private AuthenticationSuccessHandler failureHandler;

    public JwtAuthenticationFilter() {
        this.requiresAuthenticationRequestMatcher = new RequestHeaderRequestMatcher("Authorization");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logger.info("beginning===");
        if (!requiresAuthenticationRequestMatcher.matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = StringUtils.removeStart(request.getHeader("Authorization"), "Bearer").trim();
        DecodedJWT decodedJWT = null;
        Authentication authentication = null;
        AuthenticationException failed = null;
        if (StringUtils.isNotBlank(token)) {
            try {
                decodedJWT = JWT.decode(token);
                JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(decodedJWT);
                authentication = authenticationManager.authenticate(jwtAuthenticationToken);
            } catch (JWTDecodeException e) {
                failed = new InsufficientAuthenticationException("JWT format error", failed);
                unsuccessfulAuthentication(request, response, failed);
                return;
//                throw new InsufficientAuthenticationException("JWT format error");
            } catch (AuthenticationException e) {
                failed = e;
                unsuccessfulAuthentication(request, response, failed);
                return;
//                throw new InsufficientAuthenticationException("JWT format error");
            } catch (IllegalArgumentException e) {
                failed = new InsufficientAuthenticationException("JWT format error", failed);
                unsuccessfulAuthentication(request, response, failed);
            }
        } else {
            failed = new InsufficientAuthenticationException("JWT is Empty");
            unsuccessfulAuthentication(request, response, failed);
            return;
//            throw new InsufficientAuthenticationException("JWT is Empty");
        }
        if(authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            successHandler.onAuthenticationSuccess(request, response, authentication);
        } else {
            unsuccessfulAuthentication(request, response, failed);
            return;
        }

        filterChain.doFilter(request, response);
    }

    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response, AuthenticationException failed)
            throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        failureHandler.onAuthenticationFailure(request, response, failed);
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public void setAuthenticationFailureHandler(AuthenticationFailureHandler failureHandler) {
        this.failureHandler = failureHandler;
    }
}

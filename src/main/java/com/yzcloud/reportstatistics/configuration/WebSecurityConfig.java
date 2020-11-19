package com.yzcloud.reportstatistics.configuration;


import com.yzcloud.reportstatistics.service.JwtAuthenticationProvider;
import com.yzcloud.reportstatistics.service.JwtUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.header.Header;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .csrf().disable()
                .formLogin().disable()
                .sessionManagement().disable()
                .cors()
                .and()   //添加header设置，支持跨域和ajax请求
                .headers().addHeaderWriter(new StaticHeadersWriter(Arrays.asList(
                new Header("Access-control-Allow-Origin","*"),
                new Header("Access-Control-Expose-Headers","Authorization"))))
                .and() //拦截OPTIONS请求，直接返回header
                .apply(new JsonLoginConfigurer<>()).loginSuccessHandler(jsonLoginSuccessHandler())
                .and()
                .apply(new JwtLoginConfigurer<>())
                .and()
                .logout()
                .addLogoutHandler(tokenClearLogoutHandler())
                .logoutSuccessHandler(new JwtLogoutSuccessHandler())
                .and()
                .sessionManagement().disable();
        /**
         * @todo logout操作处理
         */

    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider()).authenticationProvider(jwtAuthenticationProvider());
    }

    @Bean
    protected AuthenticationProvider jwtAuthenticationProvider() {
        return new JwtAuthenticationProvider(jwtUserService());
    }

    @Bean
    protected AuthenticationProvider daoAuthenticationProvider() throws Exception{
        //这里会默认使用BCryptPasswordEncoder比对加密后的密码，注意要跟createUser时保持一致
        DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
        daoProvider.setUserDetailsService(jwtUserService());
        return daoProvider;
    }

    @Bean
    protected JsonLoginSuccessHandler jsonLoginSuccessHandler() {
        return new JsonLoginSuccessHandler(jwtUserService());
    }

    @Bean
    protected TokenClearLogoutHandler tokenClearLogoutHandler() {
        return new TokenClearLogoutHandler(jwtUserService());
    }

    @Bean
    protected JwtUserService jwtUserService() {
        return new JwtUserService();
    }

    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST","HEAD", "OPTION"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.addExposedHeader("Authorization");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

package com.joje.palanoto.common.config;

import com.joje.palanoto.common.filter.ExceptionHandlerFilter;
import com.joje.palanoto.common.filter.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final JwtTokenFilter jwtTokenFilter;

    private final ExceptionHandlerFilter exceptionHandlerFilter;

    /**
     * Security 로직에 필터를 등록
     */
    @Override
    public void configure(HttpSecurity http) {
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(exceptionHandlerFilter, JwtTokenFilter.class);
    }
}

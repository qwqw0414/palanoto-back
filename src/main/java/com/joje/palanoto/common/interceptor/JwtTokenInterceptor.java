package com.joje.palanoto.common.interceptor;

import com.joje.palanoto.common.security.JwtTokenProvider;
import com.joje.palanoto.exception.InvalidTokenException;
import com.joje.palanoto.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JWT 토큰이 올바른지 판단 잘 못 됐다면 UnauthorizedException 발생
 */
@Slf4j
@Component("JwtTokenInterceptor")
@RequiredArgsConstructor
public class JwtTokenInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider tokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        String jwt = tokenProvider.resolveToken(request);

        if(StringUtils.hasText(jwt)){
            tokenProvider.validateToken(jwt);
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

}

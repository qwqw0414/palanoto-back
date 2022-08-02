package com.joje.palanoto.common.interceptor;

import com.joje.palanoto.common.security.JwtTokenProvider;
import com.joje.palanoto.exception.InvalidTokenException;
import com.joje.palanoto.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
        try {
            tokenProvider.validateToken(request.getHeader(JwtTokenProvider.AUTHORIZATION_HEADER));
        } catch (InvalidTokenException e) {
            e.printStackTrace();
            throw new UnauthorizedException();
        } catch (NullPointerException e){
            log.debug("[token]=[{}]", request.getHeader(JwtTokenProvider.AUTHORIZATION_HEADER));
            e.printStackTrace();
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

}

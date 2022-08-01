package com.joje.palanoto.common.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component(value = "CommonInterceptor")
public class CommonInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        String requestUri = request.getRequestURI();

        if(!requestUri.startsWith("/palanoto")) {
            log.info("Invalid Uri : [requestUri]=[{}]", requestUri);
            return false;
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}

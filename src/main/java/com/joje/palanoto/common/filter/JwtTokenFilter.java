package com.joje.palanoto.common.filter;

import com.joje.palanoto.common.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;

    /**
     * 실제 필터링 로직은 doFilter 내부에 작성 jwt 토큰의 인증 정보를 SecurityContext에 저장하는 역할
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // request에서 jwt 토큰 정보 추출
        String jwt = tokenProvider.resolveToken(request);
        String requestURI = request.getRequestURI();

        // token 유효성 검증에 통과하면
        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
            Authentication authentication = tokenProvider.getAuthentication(jwt); // 정상 토큰이면 SecurityContext 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
//            log.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
        }

        filterChain.doFilter(request, response);
    }
}

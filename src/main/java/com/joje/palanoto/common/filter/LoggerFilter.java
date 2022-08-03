package com.joje.palanoto.common.filter;

import com.joje.palanoto.common.utils.HttpUtils;
import com.joje.palanoto.common.utils.ReadableRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@Order(value = 1)
@WebFilter(urlPatterns = "/palanoto/*")
public class LoggerFilter implements Filter {

    private static final String[] EXCLUDE_PATHS = {"/assets/"};

    /**
     * 요청 정보 로깅 필터
     */
    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String uri = httpRequest.getRequestURI();
        String method = httpRequest.getMethod();
        String contentType = request.getContentType();

        log.debug("=================== >> {} START {} >> ===================", method, uri);
        log.debug("ContentType : {}", contentType);
        if (contentType != null && contentType.startsWith("application/json")) {
            ReadableRequestWrapper requestWrapper = new ReadableRequestWrapper((HttpServletRequest) request);
            log.debug("RequestBody : {}", HttpUtils.getBody(requestWrapper));
            chain.doFilter(requestWrapper, response);
        } else {
            chain.doFilter(request, response);
        }

//        response.setContentType("application/json; charset=utf8");
        log.debug("=================== << {} END {} << ===================\n", method, uri);
    }
}
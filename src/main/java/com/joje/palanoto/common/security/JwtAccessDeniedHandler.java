package com.joje.palanoto.common.security;

import com.joje.palanoto.exception.ForbiddenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 필요한 권한이 존재하지 않는 경우 403 Forbidden 에러를 리턴
 */
@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {


//        log.debug("403 Forbidden - 권한 없음 ");
        // 필요한 권한이 없이 접근하려 할 때 403
//        response.sendError(HttpServletResponse.SC_FORBIDDEN);
        throw new ForbiddenException("해당 컨텐츠 권한이 없음");
    }
}

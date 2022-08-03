package com.joje.palanoto.common.filter;

import com.google.gson.Gson;
import com.joje.palanoto.common.constants.StatusType;
import com.joje.palanoto.exception.ExpiredTokenException;
import com.joje.palanoto.exception.ForbiddenException;
import com.joje.palanoto.exception.InvalidTokenException;
import com.joje.palanoto.exception.UnauthorizedException;
import com.joje.palanoto.model.vo.ResultVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.security.sasl.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 필터에서 발생한 예외처리 핸들링 필터
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    private final Gson gson;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try{
            filterChain.doFilter(request, response);
        } catch (UnauthorizedException e){
            log.error(e.getMessage());
            setErrorResponse(HttpStatus.UNAUTHORIZED, response, StatusType.UNAUTHORIZED);
        } catch (InvalidTokenException e) {
            log.error(e.getMessage());
            setErrorResponse(HttpStatus.UNAUTHORIZED, response, StatusType.INVALID_TOKEN);
        } catch (ExpiredTokenException e) {
            log.error(e.getMessage());
            setErrorResponse(HttpStatus.UNAUTHORIZED, response, StatusType.EXPIRED_TOKEN);
        } catch (ForbiddenException e){
            log.error(e.getMessage());
            setErrorResponse(HttpStatus.FORBIDDEN, response, StatusType.FORBIDDEN);
        } catch (Exception e) {
            log.error(e.getMessage());
            setErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, response, StatusType.SERVER_ERROR);
        }
    }

    public void setErrorResponse(HttpStatus status, HttpServletResponse response, StatusType statusType){
        response.setStatus(status.value());
        response.setContentType("application/json; charset=utf8");
        try{
            ResultVo resultVo = new ResultVo(statusType);
            response.getWriter().write(gson.toJson(resultVo));
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}

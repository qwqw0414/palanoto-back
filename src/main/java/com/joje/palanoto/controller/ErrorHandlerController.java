package com.joje.palanoto.controller;

import com.joje.palanoto.common.constants.StatusType;
import com.joje.palanoto.exception.ExpiredTokenException;
import com.joje.palanoto.exception.HttpRequestException;
import com.joje.palanoto.exception.InvalidTokenException;
import com.joje.palanoto.exception.UnauthorizedException;
import com.joje.palanoto.model.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 공통 에러 핸들링 컨트롤러
 */
@Slf4j
@RestControllerAdvice
public class ErrorHandlerController {

    private static final HttpHeaders HTTP_HEADERS = new HttpHeaders(); // 공통 헤더
    public ErrorHandlerController() {
        HTTP_HEADERS.add("Content-Type", "application/json;charset=UTF-8");
    }


    /**
     * Http 요청 실패
     */
    @ExceptionHandler(value = { HttpRequestException.class })
    public ResponseEntity<ResultVo> httpRequestExceptionHandler(HttpRequestException e) {
        log.error(e.getMessage());
        ResultVo resultVo = new ResultVo(StatusType.HTTP_REQUEST_FAILED);
        return new ResponseEntity<>(resultVo, HTTP_HEADERS, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 올바르지 않은 메소드 타입
     */
    @ExceptionHandler(value = { HttpRequestMethodNotSupportedException.class })
    public ResponseEntity<ResultVo> methodeNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException e) {
        ResultVo resultVo = new ResultVo(StatusType.METHOD_NOT_SUPPORTED);
        return new ResponseEntity<>(resultVo, HTTP_HEADERS, HttpStatus.METHOD_NOT_ALLOWED);
    }

    /**
     * 사용자 인증 실패
     */
    @ExceptionHandler(value = { UnauthorizedException.class })
    public ResponseEntity<ResultVo> unauthorizedExceptionHandler(UnauthorizedException e) {
        ResultVo resultVo = new ResultVo(StatusType.UNAUTHORIZED);
        return new ResponseEntity<>(resultVo, HTTP_HEADERS, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(value = { InvalidTokenException.class })
    public ResponseEntity<ResultVo> unauthorizedExceptionHandler(InvalidTokenException e) {
        ResultVo resultVo = new ResultVo(StatusType.INVALID_TOKEN);
        return new ResponseEntity<>(resultVo, HTTP_HEADERS, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(value = { ExpiredTokenException.class })
    public ResponseEntity<ResultVo> unauthorizedExceptionHandler(ExpiredTokenException e) {
        ResultVo resultVo = new ResultVo(StatusType.EXPIRED_TOKEN);
        return new ResponseEntity<>(resultVo, HTTP_HEADERS, HttpStatus.UNAUTHORIZED);
    }

    /**
     * 권한 인증 실패
     */
    @ExceptionHandler(value = { AccessDeniedException.class })
    public ResponseEntity<ResultVo> accessDeniedExceptionHandler(AccessDeniedException e) {
        ResultVo resultVo = new ResultVo(StatusType.FORBIDDEN);
        return new ResponseEntity<>(resultVo, HTTP_HEADERS, HttpStatus.FORBIDDEN);
    }

    /**
     * 공통 에러
     */
    @ExceptionHandler(value = { RuntimeException.class })
    public ResponseEntity<ResultVo> runtimeExceptionHandler(RuntimeException e) {
        log.error(e.getMessage());
        e.printStackTrace();

        ResultVo resultVo = new ResultVo(StatusType.SERVER_ERROR);

        return new ResponseEntity<>(resultVo, HTTP_HEADERS, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = { Exception.class })
    public ResponseEntity<ResultVo> exceptionHandler(Exception e) {
        log.error(e.getMessage());
        e.printStackTrace();

        ResultVo resultVo = new ResultVo(StatusType.SERVER_ERROR);

        return new ResponseEntity<>(resultVo, HTTP_HEADERS, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}

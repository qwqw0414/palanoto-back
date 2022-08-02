package com.joje.palanoto.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
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



}

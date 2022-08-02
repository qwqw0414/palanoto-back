package com.joje.palanoto.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusType {

    SUCCESS("C2000", "정상적으로 처리 완료"),
    BAD_REQUEST("C4000", "잘 못된 요청"),
    UNAUTHORIZED("C4010", "사용자가 승인되지 않음"),
    FORBIDDEN("C4030", "해당 컨텐츠에 접근할 권한이 없음"),
    SERVER_ERROR("C5000", "알 수 없는 서버 오류");

    private String code;
    private String message;

}

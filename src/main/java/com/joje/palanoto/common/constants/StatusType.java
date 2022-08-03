package com.joje.palanoto.common.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusType {

    SUCCESS("C2000", "정상적으로 처리 완료"),
    BAD_REQUEST("C4000", "잘 못된 요청"),
    UNAUTHORIZED("C4010", "사용자가 승인되지 않음"),
    INVALID_TOKEN("C4011", "유효하지 않은 토큰"),
    EXPIRED_TOKEN("C4012", "만료된 토큰"),
    FORBIDDEN("C4030", "해당 컨텐츠에 접근할 권한이 없음"),
    METHOD_NOT_SUPPORTED("C4050", "올바르지 않은 메소드 형식"),
    SERVER_ERROR("C5000", "알 수 없는 서버 오류");

    private final String code;
    private final String message;

}

package com.joje.palanoto.common.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusType {

//    요청 성공
    SUCCESS("C2000", "정상적으로 처리 완료"),

//    서버 에러 관련
    HTTP_REQUEST_FAILED("C3090", "HTTP 요청 실패"),

    NOT_FOUND_DATA("C3010", "일치하는 데이터가 없음"),

//    HTTP 관련
    BAD_REQUEST("C4000", "잘못된 요청"),
    UNAUTHORIZED("C4010", "사용자가 승인되지 않음"),
    INVALID_TOKEN("C4011", "유효하지 않은 토큰"),
    EXPIRED_TOKEN("C4012", "만료된 토큰"),
    FORBIDDEN("C4030", "해당 컨텐츠에 접근할 권한이 없음"),
    METHOD_NOT_SUPPORTED("C4050", "올바르지 않은 메소드 형식"),
    SERVER_ERROR("C5000", "알 수 없는 서버 오류");

    private final String code;
    private final String message;

}

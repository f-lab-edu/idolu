package com.idolu.user.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseCode {

    SUCCESS("USER-0000", "요청 성공"),
    INVALID_REQUEST("USER-0001", "잘못된 요청"),
    USER_NOT_FOUND("USER-0002", "유저 정보 미조회"),
    USER_ALREADY_EXIST("USER-0003", "중복된 사용자"),
    ROLE_NOT_FOUND("USER-0004", "권한 정보 미조회"),
    INVALID_ACCESS_TOKEN("USER-0005", "유효하지 않은 액세스 토큰"),
    INVALID_REFRESH_TOKEN("USER-0006", "유효하지 않은 리프레시 토큰"),
    SERVER_ERROR("USER-1999", "서버 오류")
    ;

    private final String detailCode;
    private final String message;
}

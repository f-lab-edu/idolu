package com.idolu.user.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "USER-001", "유저 정보를 조회할 수 없습니다."),
    USER_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "USER-002", "이미 가입된 유저 정보입니다."),
    ;

    private final HttpStatus status;
    private final String detailCode;
    private final String message;
}

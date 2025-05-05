package com.idolu.user.global.exception;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    private final ErrorCode errorCode;

    public BaseException(ErrorCode code) {
        super(code.getMessage());
        this.errorCode = code;
    }

    public BaseException(ErrorCode code, String message) {
        super(message);
        this.errorCode = code;
    }
}

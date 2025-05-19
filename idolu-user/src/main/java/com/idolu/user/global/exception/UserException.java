package com.idolu.user.global.exception;

import lombok.Getter;

@Getter
public class UserException extends RuntimeException {

    private final ResponseCode errorCode;

    public UserException(ResponseCode code) {
        super(code.getMessage());
        this.errorCode = code;
    }

    public UserException(ResponseCode code, String message) {
        super(message);
        this.errorCode = code;
    }
}

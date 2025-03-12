package com.idolu.product.global.exception;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    private final ErrorCode errorCode;

    public BaseException(ErrorCode code) {
        super(code.getMessage());
        this.errorCode = code;
    }
}

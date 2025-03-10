package com.idolu.product.global.exception;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    private final ErrorCode code;

    public BaseException(ErrorCode code) {
        super(code.getMessage());
        this.code = code;
    }
}

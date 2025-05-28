package com.idolu.product.global.common;

import lombok.Getter;

@Getter
public class ProductBadRequestException extends RuntimeException {

    private final ResponseCode errorCode;

    public ProductBadRequestException(ResponseCode code) {
        super(code.getMessage());
        this.errorCode = code;
    }

    public ProductBadRequestException(ResponseCode code, String message) {
        super(message);
        this.errorCode = code;
    }
}

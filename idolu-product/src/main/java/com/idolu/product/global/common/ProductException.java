
package com.idolu.product.global.common;

import lombok.Getter;

@Getter
public class ProductException extends RuntimeException {

    private final ResponseCode errorCode;

    public ProductException(ResponseCode code) {
        super(code.getMessage());
        this.errorCode = code;
    }

    public ProductException(ResponseCode code, String message) {
        super(message);
        this.errorCode = code;
    }
}

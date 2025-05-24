
package com.idolu.idoluorder.global.common;

import lombok.Getter;

@Getter
public class OrderException extends RuntimeException {

    private final ResponseCode errorCode;

    public OrderException(ResponseCode code) {
        super(code.getMessage());
        this.errorCode = code;
    }

    public OrderException(ResponseCode code, String message) {
        super(message);
        this.errorCode = code;
    }
}

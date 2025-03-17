package com.idolu.product.global.exception;

public class ProductNotFoundException extends BaseException {

    public ProductNotFoundException(ErrorCode code, String message) {
        super(code, message);
    }
}

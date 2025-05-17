package com.idolu.product.global.exception;

public class ProductImageNotFoundException extends BaseException {

    public ProductImageNotFoundException(ErrorCode code, String message) {
        super(code, message);
    }
}

package com.idolu.product.global.exception;

public class ProductCreateValidationException extends BaseException {

    public ProductCreateValidationException(ErrorCode code) {
        super(code);
    }
}

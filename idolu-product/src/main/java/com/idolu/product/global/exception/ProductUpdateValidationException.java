package com.idolu.product.global.exception;

public class ProductUpdateValidationException extends BaseException {

    public ProductUpdateValidationException(ErrorCode code) {
        super(code);
    }
}

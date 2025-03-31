package com.idolu.product.global.exception;

public class ProductUpdateException extends BaseException {

    public ProductUpdateException(ErrorCode code, String message) {
        super(code, message);
    }
}

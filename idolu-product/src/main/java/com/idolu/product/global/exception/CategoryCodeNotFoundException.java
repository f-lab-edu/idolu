package com.idolu.product.global.exception;

public class CategoryCodeNotFoundException extends BaseException {

    public CategoryCodeNotFoundException(ErrorCode code, String message) {
        super(code, message);
    }
}

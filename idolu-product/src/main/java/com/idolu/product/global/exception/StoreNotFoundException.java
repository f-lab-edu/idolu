package com.idolu.product.global.exception;

public class StoreNotFoundException extends BaseException {

    public StoreNotFoundException(ErrorCode code, String message) {
        super(code, message);
    }
}

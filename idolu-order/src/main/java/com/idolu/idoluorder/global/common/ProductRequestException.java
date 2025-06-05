package com.idolu.idoluorder.global.common;

import lombok.Getter;

@Getter
public class ProductRequestException extends RuntimeException {

    private final String code;
    private final String response;

    public ProductRequestException(String code, String response) {
        this.code = code;
        this.response = response;
    }
}

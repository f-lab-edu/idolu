package com.idolu.idoluorder.infrastructure.out.web.response;

import lombok.Data;

@Data
public class ProductApiResponse<T> {

    private String code;
    private String message;
    private T data;
}

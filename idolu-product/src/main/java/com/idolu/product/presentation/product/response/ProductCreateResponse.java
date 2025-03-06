package com.idolu.product.presentation.product.response;

import lombok.Getter;

@Getter
public class ProductCreateResponse {

    private Long productId;

    public ProductCreateResponse(Long id) {
        this.productId = id;
    }

    public static ProductCreateResponse from(Long id) {
        return new ProductCreateResponse(id);
    }
}

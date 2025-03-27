package com.idolu.product.presentation.product.response;

import lombok.Getter;

@Getter
public class ProductUpdateResponse {

    private Long productId;

    public ProductUpdateResponse(Long id) {
        this.productId = id;
    }

    public static ProductUpdateResponse from(Long id) {
        return new ProductUpdateResponse(id);
    }
}

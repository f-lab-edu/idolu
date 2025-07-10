package com.idolu.idoluorder.infrastructure.out.web.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductStockUpdateRequest {

    private Long productId;
    private String orderNo;
    private Integer stock;
    private String stockType;

    @Builder
    public ProductStockUpdateRequest(Long productId, String orderNo, Integer stock, String stockType) {
        this.productId = productId;
        this.orderNo = orderNo;
        this.stock = stock;
        this.stockType = stockType;
    }
}

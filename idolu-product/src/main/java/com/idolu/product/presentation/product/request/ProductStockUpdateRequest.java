package com.idolu.product.presentation.product.request;

import com.idolu.product.application.product.command.ProductStockUpdateCommand;
import lombok.Getter;

@Getter
public class ProductStockUpdateRequest {

    private Long productId;
    private Integer stock;
    private String stockType;

    public ProductStockUpdateCommand toCommand() {
        return ProductStockUpdateCommand.builder()
                .productId(this.productId)
                .stock(this.stock)
                .stockType(this.stockType)
                .build();
    }
}

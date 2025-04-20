package com.idolu.product.presentation.product.request;

import com.idolu.product.application.product.command.ProductSearchCommand;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSearchRequest {

    private String storeCode;

    private String categoryCode;

    private Long lastProductId;

    public ProductSearchCommand toCommand() {
        return ProductSearchCommand.builder()
                .storeCode(this.storeCode)
                .categoryCode(this.categoryCode)
                .lastProductId(this.lastProductId)
                .build();
    }
}

package com.idolu.product.application.product.command;

import com.idolu.product.domain.product.type.StockType;
import lombok.Builder;
import lombok.Getter;

import static com.idolu.product.domain.product.type.StockType.toStockType;

@Getter
public class ProductStockUpdateCommand {

    private Long productId;
    private Integer stock;
    private StockType stockType;

    @Builder
    public ProductStockUpdateCommand(Long productId, Integer stock, String stockType) {
        this.productId = productId;
        this.stock = stock;
        this.stockType = toStockType(stockType);
    }
}

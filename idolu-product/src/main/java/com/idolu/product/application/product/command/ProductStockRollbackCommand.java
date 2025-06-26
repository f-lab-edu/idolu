package com.idolu.product.application.product.command;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ProductStockRollbackCommand {

    private Long productId;
    private String orderNo;
    private Integer quantity;
}

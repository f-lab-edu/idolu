package com.idolu.product.presentation.product.request;

import com.idolu.product.application.product.command.ProductSearchCommand;
import com.idolu.product.domain.product.type.SortType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSearchRequest {

    private String storeCode;

    private Long categoryId;

    private String sortType;

    private Long lastProductId;

    private Integer itemCount;

    public ProductSearchCommand toCommand() {
        return ProductSearchCommand.builder()
                .storeCode(this.storeCode)
                .categoryId(this.categoryId)
                .sortType(this.sortType)
                .lastProductId(this.lastProductId)
                .itemCount(this.itemCount <= 100 ? itemCount : 20)
                .build();
    }
}

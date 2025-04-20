package com.idolu.product.application.product.command;

import com.idolu.product.domain.product.type.SortType;
import lombok.Builder;
import lombok.Getter;

import static com.idolu.product.domain.product.type.SortType.toSortType;

@Getter
public class ProductSearchCommand {

    private String storeCode;

    private String categoryCode;

    private SortType sortType;

    private String lastProductId;

    private Integer itemCount;

    @Builder
    public ProductSearchCommand(String storeCode, String categoryCode, String sortType, String lastProductId, Integer itemCount) {
        this.storeCode = storeCode;
        this.categoryCode = categoryCode;
        this.sortType = toSortType( sortType);
        this.lastProductId = lastProductId;
        this.itemCount = itemCount;
    }
}

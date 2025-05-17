package com.idolu.product.application.product.command;

import com.idolu.product.domain.product.type.SortType;
import lombok.Builder;
import lombok.Getter;

import static com.idolu.product.domain.product.type.SortType.toSortType;

@Getter
public class GetProductsByCategoryAndStoreCommand {

    private String storeCode;

    private Long categoryId;

    private SortType sortType;

    private Long lastProductId;

    private Integer itemCount;

    @Builder
    public GetProductsByCategoryAndStoreCommand(String storeCode, Long categoryId, String sortType, Long lastProductId, Integer itemCount) {
        this.storeCode = storeCode;
        this.categoryId = categoryId;
        this.sortType = toSortType(sortType);
        this.lastProductId = lastProductId;
        this.itemCount = itemCount;
    }
}

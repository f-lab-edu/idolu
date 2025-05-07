package com.idolu.product.presentation.product.request;

import com.idolu.product.application.product.command.GetProductsByCategoryAndStoreCommand;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetProductsByCategoryAndStoreRequest {

    private String storeCode;

    private Long categoryId;

    private String sortType;

    private Long lastProductId;

    private Integer itemCount;

    public GetProductsByCategoryAndStoreCommand toCommand() {
        return GetProductsByCategoryAndStoreCommand.builder()
                .storeCode(this.storeCode)
                .categoryId(this.categoryId)
                .sortType(this.sortType)
                .lastProductId(this.lastProductId)
                .itemCount(this.itemCount)
                .build();
    }
}

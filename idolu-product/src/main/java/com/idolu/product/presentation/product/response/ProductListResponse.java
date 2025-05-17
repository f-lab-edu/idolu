package com.idolu.product.presentation.product.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ProductListResponse {

    private List<ProductItemDto> products;
}

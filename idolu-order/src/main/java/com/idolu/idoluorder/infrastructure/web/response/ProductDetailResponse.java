package com.idolu.idoluorder.infrastructure.web.response;

import lombok.Getter;

@Getter
public class ProductDetailResponse {

    private Long productId;

    private String name;

    private PriceInformationDto price;

}

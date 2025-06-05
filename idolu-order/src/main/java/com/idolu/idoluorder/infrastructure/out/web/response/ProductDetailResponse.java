package com.idolu.idoluorder.infrastructure.out.web.response;

import lombok.Getter;

@Getter
public class ProductDetailResponse {

    private Long productId;

    private String name;

    private PriceInformationDto price;

}

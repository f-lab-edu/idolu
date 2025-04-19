package com.idolu.product.presentation.product.request;

import lombok.Getter;

@Getter
public class ProductImageCreateDto {

    private String imageType;

    private String url;

    private Integer sortNumber;
}

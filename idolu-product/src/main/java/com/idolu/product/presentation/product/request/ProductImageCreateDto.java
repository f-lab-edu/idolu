package com.idolu.product.presentation.product.request;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ProductImageCreateDto {

    private String imageType;

    private String url;

    private Integer sortNumber;
}

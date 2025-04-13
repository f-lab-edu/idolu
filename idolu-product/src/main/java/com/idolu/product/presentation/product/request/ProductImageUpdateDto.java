package com.idolu.product.presentation.product.request;

import lombok.Getter;
import lombok.ToString;

@Getter
public class ProductImageUpdateDto {

    private String imageType;

    private String url;

    private Integer sortNumber;
}

package com.idolu.product.presentation.product.request;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ProductSaveRequest {

    private String name;
    private Integer stock;
    private String imageUrl;
    private String description;
    private BigDecimal price;
    private String status;
}

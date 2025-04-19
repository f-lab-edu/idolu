package com.idolu.product.presentation.product.request;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ProductDiscountUpdateDto {

    private Integer discountRound;

    private BigDecimal discountValue;

    private String discountCode;
}

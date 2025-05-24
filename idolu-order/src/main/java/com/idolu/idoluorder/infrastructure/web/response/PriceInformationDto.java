package com.idolu.idoluorder.infrastructure.web.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class PriceInformationDto {

    private BigDecimal basicPrice;

    private BigDecimal sellingPrice;

    private Integer discountRate;
}

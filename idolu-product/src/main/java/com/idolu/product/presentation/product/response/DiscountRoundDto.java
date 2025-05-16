package com.idolu.product.presentation.product.response;

import com.idolu.product.domain.product.type.DiscountCode;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class DiscountRoundDto {

    private DiscountCode discountCode; // 할인 유형

    private Integer discountRound; // 적용 회차

    private BigDecimal discountValue; // 할인 금액/할인율

}

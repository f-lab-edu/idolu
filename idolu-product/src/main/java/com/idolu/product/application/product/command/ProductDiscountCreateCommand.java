package com.idolu.product.application.product.command;

import com.idolu.product.domain.product.type.DiscountCode;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

import static com.idolu.product.domain.product.type.DiscountCode.toDiscountCode;

@Getter
public class ProductDiscountCreateCommand {

    private Integer discountRound;

    private BigDecimal discountValue;

    private DiscountCode discountCode;

    @Builder
    public ProductDiscountCreateCommand(Integer discountRound, BigDecimal discountValue, String discountCode) {
        this.discountRound = discountRound;
        this.discountValue = discountValue;
        this.discountCode = toDiscountCode(discountCode);
    }
}

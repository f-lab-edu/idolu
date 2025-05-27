package com.idolu.product.domain.product.type;

import java.util.Arrays;

public enum DiscountCode {
    AMOUNT,
    RATE;

    public static DiscountCode toDiscountCode(String code) {
        return Arrays.stream(DiscountCode.values())
                .filter(discountCode -> discountCode.name().equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("타입을 찾을 수 없습니다. DiscountCode: %s".formatted(code)));
    }
}

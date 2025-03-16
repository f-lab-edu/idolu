package com.idolu.product.domain.product;

import java.util.Arrays;
import java.util.EnumSet;

public enum ProductStatus {
    COMING_SOON,
    ON_SALE,
    SOLD_OUT,
    TERMINATED
    ;

    private static final EnumSet<ProductStatus> VALID_INITIAL_STATES = EnumSet.of(COMING_SOON, ON_SALE);

    public static ProductStatus validateInitialState(String status) {
        return Arrays.stream(ProductStatus.values())
                .filter(productStatus -> VALID_INITIAL_STATES.contains(productStatus) && productStatus.name().equals(status))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("%s는 상품 초기 상태가 아닙니다.".formatted(status)));
    }
}

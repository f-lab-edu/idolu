package com.idolu.product.domain.product.type;

import com.idolu.product.global.common.ProductException;

import java.util.Arrays;
import java.util.EnumSet;

import static com.idolu.product.global.common.ResponseCode.*;


public enum ProductStatus {
    COMING_SOON,
    ON_SALE,
    SOLD_OUT,
    TERMINATED
    ;

    private static final EnumSet<ProductStatus> VALID_INITIAL_STATES = EnumSet.of(COMING_SOON, ON_SALE);

    public static ProductStatus toProductStatus(String status) {
        return Arrays.stream(ProductStatus.values())
                .filter(productStatus -> productStatus.name().equals(status))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("타입을 찾을 수 없습니다. ProductStatus: %s".formatted(status)));
    }

    public static ProductStatus validateInitialState(String status) {
        return Arrays.stream(ProductStatus.values())
                .filter(productStatus -> VALID_INITIAL_STATES.contains(productStatus) && productStatus.name().equals(status))
                .findAny()
                .orElseThrow(() -> new ProductException(PRODUCT_STATUS_VALIDATION_FAILED));
    }
}

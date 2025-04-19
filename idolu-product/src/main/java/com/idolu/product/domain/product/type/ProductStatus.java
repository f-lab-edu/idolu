package com.idolu.product.domain.product.type;

import com.idolu.product.global.exception.BaseException;
import com.idolu.product.global.exception.ProductCreateValidationException;

import java.util.Arrays;
import java.util.EnumSet;

import static com.idolu.product.global.exception.ErrorCode.ENUM_VALIDATE_FAILED;
import static com.idolu.product.global.exception.ErrorCode.PRODUCT_STATUS_VALIDATION_FAILED;

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
                .orElseThrow(() -> new BaseException(ENUM_VALIDATE_FAILED, ENUM_VALIDATE_FAILED.getMessage().formatted(status)));
    }

    public static ProductStatus validateInitialState(String status) {
        return Arrays.stream(ProductStatus.values())
                .filter(productStatus -> VALID_INITIAL_STATES.contains(productStatus) && productStatus.name().equals(status))
                .findAny()
                .orElseThrow(() -> new ProductCreateValidationException(PRODUCT_STATUS_VALIDATION_FAILED));
    }
}

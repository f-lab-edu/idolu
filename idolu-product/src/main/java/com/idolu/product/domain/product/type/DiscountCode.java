package com.idolu.product.domain.product.type;

import com.idolu.product.global.exception.BaseException;

import java.util.Arrays;

import static com.idolu.product.global.exception.ErrorCode.ENUM_VALIDATE_FAILED;

public enum DiscountCode {
    AMOUNT,
    RATE;

    public static DiscountCode toDiscountCode(String code) {
        return Arrays.stream(DiscountCode.values())
                .filter(discountCode -> discountCode.name().equals(code))
                .findAny()
                .orElseThrow(() -> new BaseException(ENUM_VALIDATE_FAILED, ENUM_VALIDATE_FAILED.getMessage().formatted(code)));
    }
}

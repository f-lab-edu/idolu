package com.idolu.product.domain.product.type;

import com.idolu.product.global.exception.BaseException;

import java.util.Arrays;

import static com.idolu.product.global.exception.ErrorCode.ENUM_VALIDATE_FAILED;

public enum PeriodUnitCode {
    YEAR,
    MONTH,
    WEEK,
    DAY;

    public PeriodUnitCode toPeriodUnitCode(String code) {
        return Arrays.stream(PeriodUnitCode.values())
                .filter(periodUnitCode -> periodUnitCode.name().equals(code))
                .findAny()
                .orElseThrow(() -> new BaseException(ENUM_VALIDATE_FAILED, ENUM_VALIDATE_FAILED.getMessage().formatted(code)));
    }
}

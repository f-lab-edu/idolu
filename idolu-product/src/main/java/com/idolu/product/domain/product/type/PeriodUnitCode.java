package com.idolu.product.domain.product.type;

import java.util.Arrays;

public enum PeriodUnitCode {
    YEAR,
    MONTH,
    WEEK,
    DAY;

    public static PeriodUnitCode toPeriodUnitCode(String code) {
        return Arrays.stream(PeriodUnitCode.values())
                .filter(periodUnitCode -> periodUnitCode.name().equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("타입을 찾을 수 없습니다. PeriodCode: %s".formatted(code)));
    }
}

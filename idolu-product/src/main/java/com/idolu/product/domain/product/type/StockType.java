package com.idolu.product.domain.product.type;

import java.util.Arrays;

public enum StockType {
    INCREASE, DECREASE;

    public static StockType toStockType(String type) {
        return Arrays.stream(StockType.values())
                .filter(stockType -> stockType.name().equals(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("타입을 찾을 수 없습니다. StockType: %s".formatted(type)));
    }
}

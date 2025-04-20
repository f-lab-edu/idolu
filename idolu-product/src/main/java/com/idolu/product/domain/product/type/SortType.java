package com.idolu.product.domain.product.type;

import java.util.Arrays;

public enum SortType {
    LATEST,
    PRICE_LOW,
    PRICE_HIGH;

    public static SortType toSortType(String type) {
        return Arrays.stream(SortType.values())
                .filter(sortType -> sortType.name().equals(type))
                .findAny()
                .orElse(LATEST); // default
    }
}

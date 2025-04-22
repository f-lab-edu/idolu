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

    public String whereSortType() {
        return switch (this) {
            case LATEST -> "ORDER BY p.created_at DESC ";
            case PRICE_LOW -> "ORDER BY p.selling_price ASC, p.created_at DESC ";
            case PRICE_HIGH -> "ORDER BY p.selling_price DESC, p.created_at DESC ";
        };
    }
}

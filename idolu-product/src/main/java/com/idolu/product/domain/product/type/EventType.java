package com.idolu.product.domain.product.type;

import java.util.Arrays;

public enum EventType {
    INVENTORY_INCREASE, INVENTORY_DECREASE;

    public static EventType toEventType(String type) {
        return Arrays.stream(EventType.values())
                .filter(eventType -> eventType.name().equals(type))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("타입을 찾을 수 없습니다. EventType: %s".formatted(type)));
    }
}

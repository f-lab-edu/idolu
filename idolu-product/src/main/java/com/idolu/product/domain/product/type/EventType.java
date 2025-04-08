package com.idolu.product.domain.product.type;

import com.idolu.product.global.exception.BaseException;

import java.util.Arrays;

import static com.idolu.product.global.exception.ErrorCode.ENUM_VALIDATE_FAILED;

public enum EventType {
    INVENTORY_INCREASE, INVENTORY_DECREASE;

    public EventType toEventType(String type) {
        return Arrays.stream(EventType.values())
                .filter(eventType -> eventType.name().equals(type))
                .findAny()
                .orElseThrow(() -> new BaseException(ENUM_VALIDATE_FAILED, ENUM_VALIDATE_FAILED.getMessage().formatted(type)));
    }
}

package com.idolu.idoluorder.domain.payment.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum PaymentType {
    NORMAL("일반 결제");

    private final String description;

    public static PaymentType from(String type) {
        return Arrays.stream(PaymentType.values())
                .filter(paymentType -> paymentType.name().equals(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("타입을 찾을 수 없습니다. PaymentType: %s".formatted(type)));
    }
}

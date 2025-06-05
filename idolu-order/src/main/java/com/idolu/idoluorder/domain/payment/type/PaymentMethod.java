package com.idolu.idoluorder.domain.payment.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum PaymentMethod {
    ONETOUCH_PAY("간편결제");

    private final String method;

    public static PaymentMethod from(String method) {
        return Arrays.stream(PaymentMethod.values())
                .filter(paymentMethod -> paymentMethod.name().equals(method))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("타입을 찾을 수 없습니다. PaymentMethod: %s".formatted(method)));
    }
}

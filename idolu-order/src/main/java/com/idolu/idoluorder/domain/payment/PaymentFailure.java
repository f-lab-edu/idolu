package com.idolu.idoluorder.domain.payment;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentFailure {
    private String errorCode;
    private String message;
}

package com.idolu.idoluorder.infrastructure.out.web.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentExecutionRequest {

    private String paymentKey;
    private String orderId;
    private Integer amount;
}

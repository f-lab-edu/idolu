package com.idolu.idoluorder.domain.payment;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentExecutionResult {

    private String paymentKey;
    private String orderNo;
    private PaymentExtraDetails extraDetails;
    private PaymentFailure failure;
    private Boolean isSuccess;
    private Boolean isFailure;
    private Boolean isUnknown;
    private Boolean isRetryable;
}

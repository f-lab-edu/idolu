package com.idolu.idoluorder.domain.payment;

import com.idolu.idoluorder.domain.order.type.OrderStatus;
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

    public OrderStatus toOrderStatus() {
        if (Boolean.TRUE.equals(isSuccess)) return OrderStatus.SUCCESS;
        if (Boolean.TRUE.equals(isFailure)) return OrderStatus.FAILURE;
        if (Boolean.TRUE.equals(isUnknown)) return OrderStatus.UNKNWOKN;
        throw new IllegalArgumentException("결제(orderNo: %s)는 올바르지 않은 결제 상태입니다.".formatted(orderNo));
    }
}

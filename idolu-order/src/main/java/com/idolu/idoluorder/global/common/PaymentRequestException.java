package com.idolu.idoluorder.global.common;

import com.idolu.idoluorder.domain.order.type.OrderStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentRequestException extends RuntimeException {

    private String errorCode;
    private String errorMessage;
    private Boolean isSuccess;
    private Boolean isFailure;
    private Boolean isUnknown;

    public OrderStatus toOrderStatus() {
        if (Boolean.TRUE.equals(isSuccess)) return OrderStatus.CONFIRM_SUCCESS;
        if (Boolean.TRUE.equals(isFailure)) return OrderStatus.CONFIRM_FAILURE;
        if (Boolean.TRUE.equals(isUnknown)) return OrderStatus.CONFIRM_UNKNOWN;
        throw new IllegalArgumentException("올바르지 않은 결제 상태입니다.");
    }
}

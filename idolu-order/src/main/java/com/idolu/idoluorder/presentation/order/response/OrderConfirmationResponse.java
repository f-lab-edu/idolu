package com.idolu.idoluorder.presentation.order.response;

import com.idolu.idoluorder.domain.order.type.OrderStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderConfirmationResponse {

    private OrderStatus status;
    private OrderFailure failure;
}

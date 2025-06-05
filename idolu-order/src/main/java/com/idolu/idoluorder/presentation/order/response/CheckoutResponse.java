package com.idolu.idoluorder.presentation.order.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class CheckoutResponse {

    private Long orderId;
    private String orderNo;
    private BigDecimal amount;
}

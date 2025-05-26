package com.idolu.idoluorder.presentation.order.request;

import com.idolu.idoluorder.application.order.command.OrderConfirmCommand;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class OrderConfirmRequest {

    private String paymentKey;
    private Long orderId;
    private Long productId;
    private BigDecimal amount;

    public OrderConfirmCommand toCommand() {
        return OrderConfirmCommand.builder()
                .paymentKey(this.paymentKey)
                .orderId(this.orderId)
                .productId(this.productId)
                .amount(this.amount)
                .build();
    }
}

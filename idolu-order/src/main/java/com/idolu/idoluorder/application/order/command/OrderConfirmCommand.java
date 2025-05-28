package com.idolu.idoluorder.application.order.command;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class OrderConfirmCommand {

    private String paymentKey;
    private Long orderId;
    private Long productId;
    private Integer quantity;
    private BigDecimal amount;

    @Builder
    public OrderConfirmCommand(String paymentKey, Long orderId, Long productId, Integer quantity, BigDecimal amount) {
        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.amount = amount;
    }
}

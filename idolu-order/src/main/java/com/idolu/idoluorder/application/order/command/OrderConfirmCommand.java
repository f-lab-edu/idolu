package com.idolu.idoluorder.application.order.command;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class OrderConfirmCommand {

    private String paymentKey;
    private String orderNo;
    private Long productId;
    private Integer quantity;
    private BigDecimal amount;

    @Builder
    public OrderConfirmCommand(String paymentKey, String orderNo, Long productId, Integer quantity, BigDecimal amount) {
        this.paymentKey = paymentKey;
        this.orderNo = orderNo;
        this.productId = productId;
        this.quantity = quantity;
        this.amount = amount;
    }
}

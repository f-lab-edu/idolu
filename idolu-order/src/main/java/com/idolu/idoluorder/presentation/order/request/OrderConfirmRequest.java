package com.idolu.idoluorder.presentation.order.request;

import com.idolu.idoluorder.application.order.command.OrderConfirmCommand;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class OrderConfirmRequest {

    private String paymentKey;
    private String orderNo;
    private Long productId;
    private Integer quantity;
    private BigDecimal amount;

    public OrderConfirmCommand toCommand() {
        return OrderConfirmCommand.builder()
                .paymentKey(this.paymentKey)
                .orderNo(this.orderNo)
                .productId(this.productId)
                .quantity(this.quantity)
                .amount(this.amount)
                .build();
    }
}

package com.idolu.idoluorder.application.order.command;

import com.idolu.idoluorder.domain.order.OrderFailure;
import com.idolu.idoluorder.domain.order.type.OrderStatus;
import com.idolu.idoluorder.domain.payment.PaymentExtraDetails;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderStatusUpdateCommand {

    private String paymentKey;
    private String orderNo;
    private OrderStatus orderStatus;
    private PaymentExtraDetails extraDetails;
    private OrderFailure orderFailure;
    private Long productId;
    private Integer quantity;

    @Builder
    public OrderStatusUpdateCommand(String paymentKey, String orderNo, OrderStatus orderStatus, PaymentExtraDetails extraDetails, OrderFailure orderFailure, Long productId, Integer quantity) {
        this.paymentKey = paymentKey;
        this.orderNo = orderNo;
        this.orderStatus = orderStatus;
        this.extraDetails = extraDetails;
        this.orderFailure = orderFailure;
        this.productId = productId;
        this.quantity = quantity;
    }
}

package com.idolu.idoluorder.application.order.command;

import com.idolu.idoluorder.domain.order.type.OrderStatus;
import com.idolu.idoluorder.domain.payment.PaymentExtraDetails;
import com.idolu.idoluorder.domain.payment.PaymentFailure;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PaymentStatusUpdateCommand {

    private String paymentKey;
    private String orderNo;
    private OrderStatus orderStatus;
    private PaymentExtraDetails extraDetails;
    private PaymentFailure paymentFailure;

    @Builder
    public PaymentStatusUpdateCommand(String paymentKey, String orderNo, OrderStatus orderStatus, PaymentExtraDetails extraDetails, PaymentFailure paymentFailure) {
        this.paymentKey = paymentKey;
        this.orderNo = orderNo;
        this.orderStatus = orderStatus;
        this.extraDetails = extraDetails;
        this.paymentFailure = paymentFailure;
    }
}

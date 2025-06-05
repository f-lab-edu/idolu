package com.idolu.idoluorder.domain.payment;

import com.idolu.idoluorder.domain.payment.type.PaymentStatus;
import com.idolu.idoluorder.domain.payment.type.PaymentMethod;
import com.idolu.idoluorder.domain.payment.type.PaymentType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class PaymentExtraDetails {

    private PaymentType type;
    private PaymentMethod method;
    private LocalDateTime approvedAt;
    private String orderName;
    private PaymentStatus paymentStatus;
    private BigDecimal totalAmount;
    private BigDecimal balanceAmount;
    private String pspRawData;
}

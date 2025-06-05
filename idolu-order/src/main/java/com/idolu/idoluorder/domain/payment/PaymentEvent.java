package com.idolu.idoluorder.domain.payment;

import com.idolu.idoluorder.domain.payment.type.PaymentMethod;
import com.idolu.idoluorder.domain.payment.type.PaymentStatus;
import com.idolu.idoluorder.global.common.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Table(name = "payment_event")
public class PaymentEvent extends BaseEntity {

    @Id
    private Long paymentEventId;
    private Long orderId;
    private String paymentKey;
    private PaymentMethod method;
    private PaymentStatus paymentStatus;
    private BigDecimal totalAmount;
    private BigDecimal balanceAmount;
    private BigDecimal canceledAmount;

    @Builder
    @PersistenceCreator
    public PaymentEvent(Long paymentEventId, Long orderId, String paymentKey, PaymentMethod method, PaymentStatus paymentStatus,
                        BigDecimal totalAmount, BigDecimal balanceAmount, BigDecimal canceledAmount,
                        LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(createdAt, updatedAt);
        this.paymentEventId = paymentEventId;
        this.orderId = orderId;
        this.paymentKey = paymentKey;
        this.method = method;
        this.paymentStatus = paymentStatus;
        this.totalAmount = totalAmount;
        this.balanceAmount = balanceAmount;
        this.canceledAmount = canceledAmount;
    }
}

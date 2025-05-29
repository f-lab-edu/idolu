package com.idolu.idoluorder.domain.order;

import com.idolu.idoluorder.domain.order.type.OrderStatus;
import com.idolu.idoluorder.global.common.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

import static com.idolu.idoluorder.domain.order.type.OrderStatus.EXECUTING;

@Getter
@Table(name = "orders")
public class Order extends BaseEntity {

    @Id
    private Long orderId;

    private Long buyerId;

    private String orderNo;

    private String recipient;

    private String phone;

    private String zipCode;

    private String baseAddress;

    private String detailAddress;

    private String paymentKey;

    private OrderStatus orderStatus;

    private Integer failedCount;

    private Integer threshold;

    private LocalDateTime approvedAt;

    @Transient
    private OrderItem orderItem;

    @Builder
    @PersistenceCreator
    public Order(Long orderId, Long buyerId, String orderNo, String recipient, String phone,
                 String zipCode, String baseAddress, String detailAddress, String paymentKey, OrderStatus orderStatus,
                 Integer failedCount, Integer threshold, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime approvedAt) {
        super(createdAt, updatedAt);
        this.orderId = orderId;
        this.buyerId = buyerId;
        this.orderNo = orderNo;
        this.recipient = recipient;
        this.phone = phone;
        this.zipCode = zipCode;
        this.baseAddress = baseAddress;
        this.detailAddress = detailAddress;
        this.paymentKey = paymentKey;
        this.orderStatus = orderStatus;
        this.failedCount = failedCount;
        this.threshold = threshold;
        this.approvedAt = approvedAt;
    }

    public Order withOrderItem(OrderItem orderItems) {
        this.orderItem = orderItems;
        return this;
    }

    public Order toExecutingWithPaymentKey(String paymentKey) {
        this.orderStatus = EXECUTING;
        this.paymentKey = paymentKey;
        return this;
    }

    public Order changeStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
        return this;
    }
}

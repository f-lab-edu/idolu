package com.idolu.idoluorder.domain.order;

import com.idolu.idoluorder.domain.order.type.OrderStatus;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table
@Getter
public class OrderHistory {

    @Id
    private Long orderHistoriesId;

    private Long orderId;

    private OrderStatus previousStatus;

    private OrderStatus newStatus;

    private String reason;

    @CreatedDate
    private LocalDateTime createdAt;

    @Builder
    @PersistenceCreator
    public OrderHistory(Long orderHistoriesId, Long orderId, OrderStatus previousStatus, OrderStatus newStatus, String reason, LocalDateTime createdAt) {
        this.orderHistoriesId = orderHistoriesId;
        this.orderId = orderId;
        this.previousStatus = previousStatus;
        this.newStatus = newStatus;
        this.reason = reason;
        this.createdAt = createdAt;
    }
}

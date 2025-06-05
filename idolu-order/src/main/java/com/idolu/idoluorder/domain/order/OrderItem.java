package com.idolu.idoluorder.domain.order;

import com.idolu.idoluorder.global.common.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table
@Getter
public class OrderItem extends BaseEntity {

    @Id
    private Long orderItemId;

    private Long orderId;

    private Long productId;

    private String productName;

    private BigDecimal basicPrice;

    private BigDecimal sellingPrice;

    private Integer discountRate;

    private BigDecimal amount;

    private Integer quantity;

    public OrderItem withOrderId(Long orderId) {
        this.orderId = orderId;
        return this;
    }

    @Builder
    @PersistenceCreator
    public OrderItem(Long orderItemId, Long orderId, Long productId, String productName, BigDecimal basicPrice,
                     BigDecimal sellingPrice, Integer discountRate, BigDecimal amount, Integer quantity,
                     LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(createdAt, updatedAt);
        this.orderItemId = orderItemId;
        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.basicPrice = basicPrice;
        this.sellingPrice = sellingPrice;
        this.discountRate = discountRate;
        this.amount = amount;
        this.quantity = quantity;
    }
}

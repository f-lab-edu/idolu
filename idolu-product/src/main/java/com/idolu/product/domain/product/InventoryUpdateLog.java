package com.idolu.product.domain.product;

import com.idolu.product.domain.product.type.StockType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table
@Getter
@EqualsAndHashCode(of = "inventoryUpdateLogId")
public class InventoryUpdateLog {

    @Id
    private Long inventoryUpdateLogId;

    private Long productId;

    private String orderNo;

    private Integer quantity;

    private StockType type;

    @CreatedDate
    private LocalDateTime createdAt;

    @Builder
    @PersistenceCreator
    public InventoryUpdateLog(Long inventoryUpdateLogId, Long productId, String orderNo, Integer quantity, StockType type, LocalDateTime createdAt) {
        this.inventoryUpdateLogId = inventoryUpdateLogId;
        this.productId = productId;
        this.orderNo = orderNo;
        this.quantity = quantity;
        this.type = type;
        this.createdAt = createdAt;
    }
}

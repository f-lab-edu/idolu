package com.idolu.product.domain.product;

import com.idolu.product.domain.product.type.StockUpdateType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
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

    private Integer stock;

    private StockUpdateType type;

    @CreatedDate
    private LocalDateTime createdAt;
}

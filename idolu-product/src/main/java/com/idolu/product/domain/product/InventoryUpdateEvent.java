package com.idolu.product.domain.product;

import com.idolu.product.domain.product.type.EventStatus;
import com.idolu.product.domain.product.type.EventType;
import lombok.*;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "inventoryUpdateEventId")
public class InventoryUpdateEvent {

    private Long inventoryUpdateEventId;

    private String productIdentifier;

    private String orderNumber;

    private EventType eventType;

    private EventStatus eventStatus;

    private LocalDateTime createdAt;
}

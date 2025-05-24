package com.idolu.idoluorder.infrastructure.out.persistence.r2dbc.repository;

import com.idolu.idoluorder.domain.order.OrderItem;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface OrderItemRepository extends R2dbcRepository<OrderItem, Long> {
}

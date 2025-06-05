package com.idolu.idoluorder.infrastructure.out.persistence.r2dbc.repository;

import com.idolu.idoluorder.domain.order.OrderHistory;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface OrderHistoryRepository extends R2dbcRepository<OrderHistory, Long> {
}

package com.idolu.idoluorder.infrastructure.out.persistence.r2dbc.repository;

import com.idolu.idoluorder.domain.order.Order;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface OrderRepository extends R2dbcRepository<Order, Long> {
    Mono<Order> findByOrderNo(String orderNo);
}

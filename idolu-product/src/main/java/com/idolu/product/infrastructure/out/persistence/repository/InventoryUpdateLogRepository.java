package com.idolu.product.infrastructure.out.persistence.repository;

import com.idolu.product.domain.product.InventoryUpdateLog;
import com.idolu.product.domain.product.type.StockType;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface InventoryUpdateLogRepository extends R2dbcRepository<InventoryUpdateLog, Long> {
    Mono<InventoryUpdateLog> findInventoryUpdateLogByOrderNoAndType(String orderNo, StockType type);
}

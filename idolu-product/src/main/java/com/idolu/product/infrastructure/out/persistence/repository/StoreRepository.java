package com.idolu.product.infrastructure.out.persistence.repository;

import com.idolu.product.domain.store.Store;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface StoreRepository extends R2dbcRepository<Store, Long> {

    Mono<Store> findByStoreCode(String storeCode);
}

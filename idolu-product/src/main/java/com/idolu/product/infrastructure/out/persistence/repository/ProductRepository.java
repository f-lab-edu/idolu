package com.idolu.product.infrastructure.out.persistence.repository;

import com.idolu.product.domain.product.Product;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface ProductRepository extends R2dbcRepository<Product, Long> {

    Mono<Product> findByProductIdAndDeleted(Long productId, boolean deleted);
}

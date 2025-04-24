package com.idolu.product.infrastructure.out.persistence.repository;

import com.idolu.product.domain.product.ProductDiscount;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ProductDiscountRepository extends ReactiveCrudRepository<ProductDiscount, Long> {

    Flux<ProductDiscount> findByProductId(Long productId);

    @Modifying
    @Query("UPDATE product_discount SET deleted = true WHERE product_id = :productId")
    Mono<Void> setDeletedByProductId(Long productId);

}

package com.idolu.product.infrastructure.out.persistence.repository;

import com.idolu.product.domain.product.ProductDiscount;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ProductDiscountRepository extends ReactiveCrudRepository<ProductDiscount, Long> {

    @Modifying
    @Query("UPDATE product_discount SET deleted = true WHERE product_id = :productId")
    Mono<Void> setDeletedByProductId(Long productId);
}

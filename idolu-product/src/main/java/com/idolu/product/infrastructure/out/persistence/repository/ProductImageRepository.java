package com.idolu.product.infrastructure.out.persistence.repository;

import com.idolu.product.domain.product.ProductImage;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ProductImageRepository extends ReactiveCrudRepository<ProductImage, Long> {

    @Modifying
    @Query("UPDATE product_image SET deleted = true WHERE product_id = :product_id")
    Mono<Void> setDeletedByProductId(Long productId);
}

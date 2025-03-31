package com.idolu.product.infrastructure.out.persistence.repository;

import com.idolu.product.domain.productcategory.ProductCategory;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductCategoryRepository extends R2dbcRepository<ProductCategory, Long> {

    Flux<ProductCategory> findByProductIdAndDeleted(Long productId, Boolean deleted);

    @Modifying
    @Query("UPDATE product_category SET deleted = true WHERE category_id = :categoryId AND product_id = :productId")
    Mono<Void> setDeletedByCategoryIdAndProductId(Long categoryId, Long productId);
}

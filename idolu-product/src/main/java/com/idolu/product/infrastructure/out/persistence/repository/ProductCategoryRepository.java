package com.idolu.product.infrastructure.out.persistence.repository;

import com.idolu.product.domain.productcategory.ProductCategory;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductCategoryRepository extends R2dbcRepository<ProductCategory, Long> {

    Flux<ProductCategory> findByProductId(Long productId);

    Mono<Void> deleteByCategoryIdAndProductId(Long categoryId, Long productId);
}

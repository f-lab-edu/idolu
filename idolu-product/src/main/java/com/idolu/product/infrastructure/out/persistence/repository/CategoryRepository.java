package com.idolu.product.infrastructure.out.persistence.repository;

import com.idolu.product.domain.category.Category;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CategoryRepository extends R2dbcRepository<Category, Long> {

    Mono<Category> findByCategoryIdAndDeleted(Long categoryId, Boolean deleted);
}

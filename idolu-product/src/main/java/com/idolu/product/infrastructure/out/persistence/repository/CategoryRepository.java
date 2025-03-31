package com.idolu.product.infrastructure.out.persistence.repository;

import com.idolu.product.domain.category.Category;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

import java.util.Collection;
import java.util.List;

public interface CategoryRepository extends R2dbcRepository<Category, Long> {

    Flux<Category> findByCategoryCodeInAndDeleted(Collection<String> categoryCode, Boolean deleted);
}

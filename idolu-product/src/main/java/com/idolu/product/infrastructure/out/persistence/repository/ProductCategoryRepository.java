package com.idolu.product.infrastructure.out.persistence.repository;

import com.idolu.product.domain.productcategory.ProductCategory;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface ProductCategoryRepository extends R2dbcRepository<ProductCategory, Long> {
}

package com.idolu.product.infrastructure.persistence.r2dbc.product.repository;

import com.idolu.product.domain.product.Product;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface ProductRepository extends R2dbcRepository<Product, Long> {
}

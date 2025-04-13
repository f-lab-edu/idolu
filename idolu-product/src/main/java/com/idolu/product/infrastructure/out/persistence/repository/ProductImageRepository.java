package com.idolu.product.infrastructure.out.persistence.repository;

import com.idolu.product.domain.product.ProductImage;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ProductImageRepository extends ReactiveCrudRepository<ProductImage, Long> {
}

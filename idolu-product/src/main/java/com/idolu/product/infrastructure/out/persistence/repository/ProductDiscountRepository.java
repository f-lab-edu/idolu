package com.idolu.product.infrastructure.out.persistence.repository;

import com.idolu.product.domain.product.ProductDiscount;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ProductDiscountRepository extends ReactiveCrudRepository<ProductDiscount, Long> {
}

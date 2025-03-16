package com.idolu.product.infrastructure.out.persistence.adapter;

import com.idolu.product.domain.productcategory.ProductCategory;
import com.idolu.product.infrastructure.out.persistence.repository.ProductCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ProductCategoryAdapter {

    private final ProductCategoryRepository productCategoryRepository;

    @Transactional
    public Mono<ProductCategory> saveProductCategory(ProductCategory productCategory) {
        return productCategoryRepository.save(productCategory);
    }
}

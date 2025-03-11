package com.idolu.product.infrastructure.out.persistence.adapter;

import com.idolu.product.domain.product.Product;
import com.idolu.product.domain.productcategory.ProductCategory;
import com.idolu.product.infrastructure.out.persistence.repository.ProductCategoryRepository;
import com.idolu.product.infrastructure.out.persistence.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProductAdapter {

    private final ProductRepository productRepository;
    private final ProductCategoryAdapter productCategoryAdapter;

    @Transactional
    public Mono<Long> createProduct(Product product) {
        return productRepository.save(product)
                .map(ProductCategory::from)
                .flatMapMany(Flux::fromIterable)
                .flatMap(productCategory -> productCategoryAdapter.saveProductCategory(productCategory)
                        .thenReturn(productCategory.getProductId()))
                .next();
    }
}

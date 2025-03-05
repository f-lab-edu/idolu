package com.idolu.product.infrastructure.out.persistence.adapter;

import com.idolu.product.domain.product.Product;
import com.idolu.product.infrastructure.out.persistence.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ProductAdapter {

    private final ProductRepository productRepository;

    @Transactional
    public Mono<Long> createProduct(Product product) {
        return productRepository.save(product)
                .map(Product::getId);
    }
}

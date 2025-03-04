package com.idolu.product.infrastructure.out.persistence.product.adapter;

import com.idolu.product.domain.product.Product;
import com.idolu.product.infrastructure.out.persistence.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ProductAdapter {

    private final ProductRepository productRepository;

    @Transactional
    public Mono<Product> save(Product product) {
        return productRepository.save(product);
    }
}

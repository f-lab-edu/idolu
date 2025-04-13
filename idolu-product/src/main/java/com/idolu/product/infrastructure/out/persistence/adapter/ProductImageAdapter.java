package com.idolu.product.infrastructure.out.persistence.adapter;

import com.idolu.product.domain.product.ProductImage;
import com.idolu.product.infrastructure.out.persistence.repository.ProductImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ProductImageAdapter {

    private final ProductImageRepository productImageRepository;

    public Mono<Void> saveProductImage(ProductImage productImage) {
        return productImageRepository.save(productImage)
                .then(Mono.empty());
    }
}

package com.idolu.product.infrastructure.out.persistence.adapter;

import com.idolu.product.domain.product.ProductImage;
import com.idolu.product.infrastructure.out.persistence.repository.ProductImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductImageAdapter {

    private final ProductImageRepository productImageRepository;

    @Transactional(readOnly = true)
    public Mono<List<ProductImage>> findByProductId(Long productId) {
        return productImageRepository.findByProductId(productId)
                .collectList();
    }

    public Mono<Void> saveProductImage(ProductImage productImage) {
        return productImageRepository.save(productImage)
                .then(Mono.empty());
    }

    public Mono<Void> setDeletedByProductId(Long productId) {
        return productImageRepository.setDeletedByProductId(productId);
    }
}

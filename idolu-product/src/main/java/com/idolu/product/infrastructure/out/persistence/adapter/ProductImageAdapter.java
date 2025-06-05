package com.idolu.product.infrastructure.out.persistence.adapter;

import com.idolu.product.domain.product.ProductImage;
import com.idolu.product.global.common.ProductBadRequestException;
import com.idolu.product.infrastructure.out.persistence.repository.ProductImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import static com.idolu.product.global.common.ResponseCode.PRODUCT_IMAGE_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class ProductImageAdapter {

    private final ProductImageRepository productImageRepository;

    @Transactional(readOnly = true)
    public Flux<ProductImage> findByProductId(Long productId) {
        return productImageRepository.findByProductId(productId)
                .switchIfEmpty(Mono.error(new ProductBadRequestException(PRODUCT_IMAGE_NOT_FOUND)));
    }

    public Mono<Void> saveProductImage(ProductImage productImage) {
        return productImageRepository.save(productImage)
                .then(Mono.empty());
    }

    public Mono<Void> setDeletedByProductId(Long productId) {
        return productImageRepository.setDeletedByProductId(productId);
    }
}

package com.idolu.product.infrastructure.out.persistence.adapter;

import com.idolu.product.domain.product.ProductDiscount;
import com.idolu.product.infrastructure.out.persistence.repository.ProductDiscountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductDiscountAdapter {

    private final ProductDiscountRepository productDiscountRepository;

    @Transactional(readOnly = true)
    public Mono<List<ProductDiscount>> findByProductId(Long productId) {
        return productDiscountRepository.findByProductId(productId)
                .collectList();
    }

    public Mono<Void> saveProductDiscount(ProductDiscount productDiscount) {
        return productDiscountRepository.save(productDiscount)
                .then(Mono.empty());
    }

    public Mono<Void> setDeletedByProductId(Long productId) {
        return productDiscountRepository.setDeletedByProductId(productId);
    }
}

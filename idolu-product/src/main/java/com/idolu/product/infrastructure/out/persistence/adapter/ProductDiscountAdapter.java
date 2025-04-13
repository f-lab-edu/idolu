package com.idolu.product.infrastructure.out.persistence.adapter;

import com.idolu.product.domain.product.ProductDiscount;
import com.idolu.product.infrastructure.out.persistence.repository.ProductDiscountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ProductDiscountAdapter {

    private final ProductDiscountRepository productDiscountRepository;

    public Mono<Void> saveProductDiscount(ProductDiscount productDiscount) {
        return productDiscountRepository.save(productDiscount)
                .then(Mono.empty());
    }
}

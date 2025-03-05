package com.idolu.product.application.product;

import com.idolu.product.infrastructure.out.persistence.adapter.ProductAdapter;
import com.idolu.product.presentation.product.request.ProductCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductAdapter productAdapter;

    public Mono<Long> createProduct(ProductCreateRequest productCreateRequest) {
        return productAdapter.createProduct(productCreateRequest.toEntity());
    }
}

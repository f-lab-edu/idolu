package com.idolu.product.application.product;

import com.idolu.product.domain.product.ProductStatus;
import com.idolu.product.infrastructure.out.persistence.adapter.ProductAdapter;
import com.idolu.product.presentation.product.request.ProductCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.idolu.product.domain.product.ProductStatus.validateInitialState;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductAdapter productAdapter;

    public Mono<Long> createProduct(ProductCreateRequest productCreateRequest) {
        validateInitialState(productCreateRequest.getStatus());

        return productAdapter
                .createProduct(productCreateRequest.toEntity());
    }
}

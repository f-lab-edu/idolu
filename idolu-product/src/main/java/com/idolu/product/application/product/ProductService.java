package com.idolu.product.application.product;

import com.idolu.product.application.command.ProductCreateCommand;
import com.idolu.product.infrastructure.out.persistence.adapter.ProductAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductAdapter productAdapter;

    public Mono<Long> createProduct(ProductCreateCommand command) {
        return productAdapter
                .createProduct(command.toEntity());
    }
}

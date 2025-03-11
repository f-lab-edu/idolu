package com.idolu.product.application.product;

import com.idolu.product.application.command.ProductCreateCommand;
import com.idolu.product.infrastructure.out.persistence.adapter.CategoryAdapter;
import com.idolu.product.infrastructure.out.persistence.adapter.ProductAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductAdapter productAdapter;
    private final CategoryAdapter categoryAdapter;

    public Mono<Long> createProduct(ProductCreateCommand command) {
        return categoryAdapter.validateCategoriesExist(command.getCategories())
                .collectList()
                .flatMap((categories) -> productAdapter.createProduct(command.toEntity(categories)));
    }
}

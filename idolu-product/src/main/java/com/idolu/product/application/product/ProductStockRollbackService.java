package com.idolu.product.application.product;

import com.idolu.product.application.product.command.ProductStockRollbackCommand;
import com.idolu.product.infrastructure.out.persistence.adapter.ProductAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductStockRollbackService {

    private final ProductAdapter productAdapter;

    public Mono<Boolean> stockRollback(ProductStockRollbackCommand command) {
        return productAdapter.stockRollback(command);
    }
}

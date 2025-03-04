package com.idolu.product.application.product;

import com.idolu.product.domain.product.Product;
import com.idolu.product.domain.product.ProductStatus;
import com.idolu.product.infrastructure.out.persistence.product.adapter.ProductAdapter;
import com.idolu.product.presentation.product.request.ProductSaveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductAdapter productAdapter;

    public Mono<Product> saveProduct(ProductSaveRequest productSaveRequest) {
        return productAdapter.save(Product.builder()
                .stock(productSaveRequest.getStock())
                .name(productSaveRequest.getName())
                .price(productSaveRequest.getPrice())
                .description(productSaveRequest.getDescription())
                .imageUrl(productSaveRequest.getImageUrl())
                .status(ProductStatus.ON_SALE)
                .build());
    }
}

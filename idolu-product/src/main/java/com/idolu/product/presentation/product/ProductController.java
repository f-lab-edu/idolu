package com.idolu.product.presentation.product;

import com.idolu.product.application.product.ProductService;
import com.idolu.product.domain.product.Product;
import com.idolu.product.presentation.product.request.ProductSaveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/products")
    public Mono<Product> saveProduct(@RequestBody ProductSaveRequest productSaveRequest) {
        return productService.saveProduct(productSaveRequest);
    }
}

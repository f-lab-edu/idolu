package com.idolu.product.presentation.product;

import com.idolu.product.application.product.ProductService;
import com.idolu.product.global.common.ApiResponse;
import com.idolu.product.presentation.product.request.ProductCreateRequest;
import com.idolu.product.presentation.product.request.ProductUpdateRequest;
import com.idolu.product.presentation.product.response.ProductCreateResponse;
import com.idolu.product.presentation.product.response.ProductUpdateResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ProductController {

    private final ProductService productService;

    @PostMapping("/products")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ApiResponse<ProductCreateResponse>> createProduct(@Valid @RequestBody ProductCreateRequest request) {
        log.info("request', request: {}", request);
        return productService.createProduct(request.toCommand())
                .doOnNext(id -> log.info("id {} Product 생성", id))
                .map(id -> ApiResponse.of(HttpStatus.CREATED, ProductCreateResponse.from(id)));
    }

    @PutMapping("/products")
    public Mono<ApiResponse<ProductUpdateResponse>> updateProduct(@Valid @RequestBody ProductUpdateRequest request) {
        return productService.updateProduct(request.toCommand())
                .doOnNext(id -> log.info("id {} Product 수정 완료", id))
                .map(id -> ApiResponse.ok(ProductUpdateResponse.from(id)));
    }
}

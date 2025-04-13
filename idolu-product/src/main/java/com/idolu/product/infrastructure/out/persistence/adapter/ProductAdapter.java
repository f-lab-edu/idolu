package com.idolu.product.infrastructure.out.persistence.adapter;

import com.idolu.product.domain.product.Product;
import com.idolu.product.domain.productcategory.ProductCategory;
import com.idolu.product.global.exception.ProductNotFoundException;
import com.idolu.product.global.exception.ProductUpdateException;
import com.idolu.product.infrastructure.out.persistence.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.idolu.product.global.exception.ErrorCode.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProductAdapter {

    private final ProductRepository productRepository;
    private final ProductCategoryAdapter productCategoryAdapter;
    private final ProductDiscountAdapter productDiscountAdapter;
    private final ProductImageAdapter productImageAdapter;

    @Transactional
    public Mono<Long> createProduct(Product product) {
        return productRepository.save(product)
                .flatMap(savedProduct -> Flux.fromIterable(savedProduct.getProductCategories())
                        .flatMap(productCategory -> productCategoryAdapter.saveProductCategory(productCategory.withProductId(savedProduct.getProductId())))
                        .then(Mono.just(savedProduct))) // 상품 카테고리 저장
                .flatMap(savedProduct -> Flux.fromIterable(savedProduct.getProductDiscounts())
                        .flatMap(productDiscount -> productDiscountAdapter.saveProductDiscount(productDiscount.withProductId(savedProduct.getProductId())))
                        .then(Mono.just(savedProduct))) // 할인 정보 저장
                .flatMap(savedProduct -> Flux.fromIterable(savedProduct.getProductImages())
                        .flatMap(productImage -> productImageAdapter.saveProductImage(productImage.withProductId(savedProduct.getProductId()))) // 이미지 정보 저장
                        .then(Mono.just(savedProduct.getProductId()))); // 상품 이미지 저장 후 상품 id 반환
    }

    @Transactional(readOnly = true)
    public Mono<Product> findById(Long productId) {
        return productRepository.findByProductIdAndDeleted(productId, false)
                .switchIfEmpty(Mono.error(new ProductNotFoundException(PRODUCT_NOT_FOUND, PRODUCT_NOT_FOUND.getMessage().formatted(productId))));
    }

    @Transactional
    public Mono<Product> updateProduct(Product product) {
        return productRepository.save(product)
                .onErrorMap(e -> {
                    if (e instanceof OptimisticLockingFailureException) {
                        return new ProductUpdateException(PRODUCT_DUPLICATED_REQUEST, PRODUCT_DUPLICATED_REQUEST.getMessage().formatted(product.getProductId()));
                    }

                    return e; // 다른 예외는 그대로 전달
                })
                .flatMapMany(updatedProduct -> productCategoryAdapter.findByProductId(updatedProduct.getProductId()))
                .flatMap(existingProductCategory -> productCategoryAdapter.setDeletedByCategoryIdAndProductId(existingProductCategory.getCategoryId(), existingProductCategory.getProductId())) // 기존 카테고리 삭제
                .then(Flux.fromIterable(ProductCategory.from(product)) // 새 상품 카테고리 저장
                        .flatMap(productCategoryAdapter::saveProductCategory).then())
                .thenReturn(product);
    }
}

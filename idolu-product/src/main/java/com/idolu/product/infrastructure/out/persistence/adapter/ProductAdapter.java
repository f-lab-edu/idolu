package com.idolu.product.infrastructure.out.persistence.adapter;

import com.idolu.product.domain.category.Category;
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

import java.util.HashSet;
import java.util.stream.Collectors;

import static com.idolu.product.global.exception.ErrorCode.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProductAdapter {

    private final ProductRepository productRepository;
    private final ProductCategoryAdapter productCategoryAdapter;

    @Transactional
    public Mono<Long> createProduct(Product product) {
        return productRepository.save(product)
                .map(ProductCategory::from)
                .flatMapMany(Flux::fromIterable)
                .flatMap(productCategory -> productCategoryAdapter.saveProductCategory(productCategory)
                        .thenReturn(productCategory.getProductId()))
                .next();
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
                .map(ProductCategory::getCategoryId)
                .collect(Collectors.toSet())
                .flatMap(existingProductCategoryIds -> {
                    HashSet<Long> newProductCategoryIds = product.getCategories().stream().map(Category::getCategoryId)
                            .collect(Collectors.toCollection(HashSet::new));

                    // 추가해야 할 카테고리
                    Flux<ProductCategory> productCategoriesToAdd = Flux.fromIterable(newProductCategoryIds)
                            .filter(newCategoryId -> !existingProductCategoryIds.contains(newCategoryId))
                            .map(newCategoryId -> ProductCategory.builder().categoryId(newCategoryId).productId(product.getProductId()).build());

                    // 삭제해야 할 카테고리
                    Flux<Long> productCategoriesToRemove = Flux.fromIterable(existingProductCategoryIds)
                            .filter(existingCategoryId -> !newProductCategoryIds.contains(existingCategoryId));

                    return Mono.when(
                            productCategoriesToAdd.flatMap(productCategoryAdapter::saveProductCategory),
                            productCategoriesToRemove.flatMap(categoryId -> productCategoryAdapter.deleteByCategoryIdAndProductId(categoryId, product.getProductId()))
                    );
                })
                .thenReturn(product);
    }
}

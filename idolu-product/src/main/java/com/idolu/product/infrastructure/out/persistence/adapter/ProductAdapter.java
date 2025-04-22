package com.idolu.product.infrastructure.out.persistence.adapter;

import com.idolu.product.application.product.command.ProductSearchCommand;
import com.idolu.product.domain.category.Category;
import com.idolu.product.domain.product.Product;
import com.idolu.product.domain.productcategory.ProductCategory;
import com.idolu.product.domain.store.Store;
import com.idolu.product.global.exception.ProductNotFoundException;
import com.idolu.product.global.exception.ProductUpdateException;
import com.idolu.product.global.util.SqlBuilder;
import com.idolu.product.infrastructure.out.persistence.repository.ProductRepository;
import com.idolu.product.presentation.product.response.ProductItemDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.idolu.product.domain.product.type.PeriodUnitCode.toPeriodUnitCode;
import static com.idolu.product.domain.product.type.ProductStatus.toProductStatus;
import static com.idolu.product.global.exception.ErrorCode.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProductAdapter {

    private final ProductRepository productRepository;
    private final ProductCategoryAdapter productCategoryAdapter;
    private final ProductDiscountAdapter productDiscountAdapter;
    private final ProductImageAdapter productImageAdapter;
    private final DatabaseClient databaseClient;

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
    public Flux<ProductItemDto> selectProducts(ProductSearchCommand productSearchCommand, Store store, List<Category> categories) {
        SqlBuilder sqlBuilder = new SqlBuilder("""
                            SELECT DISTINCT p.product_id,
                                p.name,
                                p.product_status,
                                p.basic_price,
                                p.selling_price,
                                p.discount_rate,
                                p.contract_period,
                                p.contract_period_unit_code,
                                p.service_period,
                                p.service_period_unit_code,
                                pi.url AS thumbnail_url,
                                p.created_at
                            FROM product p
                            JOIN store s ON s.store_id = p.store_id AND s.store_id = :storeId
                            JOIN product_category pc ON pc.product_id = p.product_id AND pc.category_id IN (:categoryIds)
                            JOIN product_image pi ON pi.product_id = p.product_id AND pi.image_type = 'MAIN' AND pi.sort_number = 1
                            WHERE p.deleted = FALSE
                              AND s.deleted = FALSE
                              AND pi.deleted = FALSE
                              AND pc.deleted = FALSE
                """);

        return sqlBuilder.appendIfPresent("AND p.product_id < :productId", productSearchCommand.getLastProductId())
                .append("ORDER BY p.created_at DESC LIMIT :limit ", productSearchCommand.getItemCount())
                .execute(databaseClient)
                .bind("storeId", store.getStoreId())
                .bind("categoryIds", categories.stream()
                        .map(Category::getCategoryId)
                        .toList())
                .bind("limit", productSearchCommand.getItemCount())
                .map((row, meta) -> ProductItemDto.builder()
                        .productId(row.get("product_id", Long.class))
                        .name(row.get("name", String.class))
                        .productStatus(toProductStatus(row.get("product_status", String.class)))
                        .basicPrice(row.get("basic_price", BigDecimal.class))
                        .sellingPrice(row.get("selling_price", BigDecimal.class))
                        .discountRate(row.get("discount_rate", Integer.class))
                        .contractPeriod(row.get("contract_period", Integer.class))
                        .contractPeriodUnitCode(toPeriodUnitCode(row.get("contract_period_unit_code", String.class)))
                        .servicePeriod(row.get("service_period", Integer.class))
                        .servicePeriodUnitCode(toPeriodUnitCode(row.get("service_period_unit_code", String.class)))
                        .thumbnailUrl(row.get("thumbnail_url", String.class))
                        .build())
                .all();
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
                .flatMap(this::deleteOldProductRelations)
                .flatMap(this::saveNewProductRelations);
    }


    private Mono<Product> deleteOldProductRelations(Product product) {
        return Mono.when(
                productCategoryAdapter.setDeletedByProductId(product.getProductId()),
                productDiscountAdapter.setDeletedByProductId(product.getProductId()),
                productImageAdapter.setDeletedByProductId(product.getProductId())
        ).thenReturn(product);
    }

    private Mono<Product> saveNewProductRelations(Product product) {
        return Flux.merge(
                Flux.fromIterable(ProductCategory.from(product)).flatMap(productCategoryAdapter::saveProductCategory),
                Flux.fromIterable(product.getProductDiscounts()).flatMap(productDiscountAdapter::saveProductDiscount),
                Flux.fromIterable(product.getProductImages()).flatMap(productImageAdapter::saveProductImage)
        ).then(Mono.just(product));
    }
}

package com.idolu.product.infrastructure.out.persistence.adapter;

import com.idolu.product.application.product.command.ProductSearchCommand;
import com.idolu.product.domain.category.Category;
import com.idolu.product.domain.product.Product;
import com.idolu.product.domain.store.Store;
import com.idolu.product.global.exception.ProductNotFoundException;
import com.idolu.product.global.exception.ProductUpdateException;
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

import static com.idolu.product.domain.product.type.PeriodUnitCode.toPeriodUnitCode;
import static com.idolu.product.domain.product.type.ProductStatus.toProductStatus;
import static com.idolu.product.global.exception.ErrorCode.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProductAdapter {

    private final ProductRepository productRepository;
    private final ProductDiscountAdapter productDiscountAdapter;
    private final ProductImageAdapter productImageAdapter;
    private final DatabaseClient databaseClient;

    @Transactional
    public Mono<Long> createProduct(Product product) {
        return productRepository.save(product)
                .flatMap(savedProduct -> Flux.fromIterable(savedProduct.getProductDiscounts())
                        .flatMap(productDiscount -> productDiscountAdapter.saveProductDiscount(productDiscount.withProductId(savedProduct.getProductId())))
                        .then(Mono.just(savedProduct))) // 할인 정보 저장
                .flatMap(savedProduct -> Flux.fromIterable(savedProduct.getProductImages())
                        .flatMap(productImage -> productImageAdapter.saveProductImage(productImage.withProductId(savedProduct.getProductId()))) // 이미지 정보 저장
                        .then(Mono.just(savedProduct.getProductId()))); // 상품 이미지 저장 후 상품 id 반환
    }

    @Transactional(readOnly = true)
    public Flux<ProductItemDto> getProductByCategoryAndStore(ProductSearchCommand productSearchCommand, Store store, Category category) {
        StringBuilder sqlBuilder = new StringBuilder("""
                            SELECT p.product_id,
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
                            JOIN product_image pi ON pi.product_id = p.product_id AND pi.image_type = 'MAIN' AND pi.sort_number = 1
                            WHERE p.category_id = :categoryId
                              AND p.deleted = FALSE
                              AND s.deleted = FALSE
                              AND pi.deleted = FALSE
                """);

        if (productSearchCommand.getLastProductId() != null) {
            sqlBuilder.append("AND p.product_id < :productId ");
        }

        sqlBuilder.append(productSearchCommand.getSortType().orderSortType());
        sqlBuilder.append("LIMIT :limit");

        DatabaseClient.GenericExecuteSpec executeSpec = databaseClient.sql(sqlBuilder.toString())
                .bind("storeId", store.getStoreId())
                .bind("categoryId", category.getCategoryId())
                .bind("limit", productSearchCommand.getItemCount());

        if (productSearchCommand.getLastProductId() != null) {
            executeSpec = executeSpec.bind("productId", productSearchCommand.getLastProductId());
        }

        return executeSpec.map((row, meta) ->
                        ProductItemDto.builder()
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
                productDiscountAdapter.setDeletedByProductId(product.getProductId()),
                productImageAdapter.setDeletedByProductId(product.getProductId())
        ).thenReturn(product);
    }

    private Mono<Product> saveNewProductRelations(Product product) {
        return Flux.merge(
                Flux.fromIterable(product.getProductDiscounts()).flatMap(productDiscountAdapter::saveProductDiscount),
                Flux.fromIterable(product.getProductImages()).flatMap(productImageAdapter::saveProductImage)
        ).then(Mono.just(product));
    }
}

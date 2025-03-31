package com.idolu.product.application.product;

import com.idolu.product.application.command.ProductCreateCommand;
import com.idolu.product.application.command.ProductUpdateCommand;
import com.idolu.product.domain.product.Product;
import com.idolu.product.global.exception.ProductUpdateException;
import com.idolu.product.infrastructure.out.persistence.adapter.CategoryAdapter;
import com.idolu.product.infrastructure.out.persistence.adapter.ProductAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.function.TupleUtils;

import static com.idolu.product.global.exception.ErrorCode.PRODUCT_INVALID_UPDATED_TIME;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductAdapter productAdapter;
    private final CategoryAdapter categoryAdapter;

    public Mono<Long> createProduct(ProductCreateCommand command) {
        return categoryAdapter.validateCategoriesExist(command.getCategories())
                .flatMap((categories) -> productAdapter.createProduct(command.toEntity(categories)));
    }

    /**
     * 1. 상품 정보 조회
     *   - 상품 정보가 없다면 예외 반환
     * 2. 마지막 수정일시 다른 경우 예외 반환
     * 3. 업데이트할 카테고리 정보 조회
     *   - 카테고리 정보가 없다면 예외 반환
     * 5. 상품 정보 업데이트
     * 6. Version 다른 경우 예외 반환
     * 7. 상품에 대한 카테고리 정보 조회 후 업데이트
     *   - 원래 있으면 그대로, 없다면 추가, 없어질 카테고리 정보는 삭제
     */
    public Mono<Long> updateProduct(ProductUpdateCommand command) {
        return productAdapter.findById(command.getProductId())
                .flatMap(product -> {
                    if (!product.getUpdatedAt().isEqual(command.getUpdatedAt())) {
                        return Mono.error(new ProductUpdateException(PRODUCT_INVALID_UPDATED_TIME, PRODUCT_INVALID_UPDATED_TIME.getMessage().formatted(product.getProductId())));
                    }

                    return Mono.just(product.changeInfo(command));
                })
                .zipWhen(product -> categoryAdapter.validateCategoriesExist(command.getCategories()))
                .map(TupleUtils.function(Product::withCategories))
                .flatMap(productAdapter::updateProduct)
                .map(Product::getProductId);
    }
}

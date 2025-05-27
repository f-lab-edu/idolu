package com.idolu.product.infrastructure.out.persistence.adapter;

import com.idolu.product.domain.category.Category;
import com.idolu.product.global.common.ProductException;
import com.idolu.product.infrastructure.out.persistence.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import static com.idolu.product.global.common.ResponseCode .*;

@Slf4j
@Component
@RequiredArgsConstructor
public class CategoryAdapter {

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Mono<Category> findByCategoryId(Long categoryId) {
        return categoryRepository.findByCategoryIdAndDeleted(categoryId, false)
                .switchIfEmpty(Mono.error(new ProductException(PRODUCT_CATEGORIES_VALIDATION_FAILED)));
    }
}

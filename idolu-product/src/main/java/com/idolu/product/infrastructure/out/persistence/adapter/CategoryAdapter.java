package com.idolu.product.infrastructure.out.persistence.adapter;

import com.idolu.product.domain.category.Category;
import com.idolu.product.global.exception.CategoryCodeNotFoundException;
import com.idolu.product.global.exception.ProductCreateValidationException;
import com.idolu.product.infrastructure.out.persistence.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.idolu.product.global.exception.ErrorCode.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class CategoryAdapter {

    private final CategoryRepository categoryRepository;
    private static final String CATEGORY_CODE_SUFFIX_LIKE = "%";

    @Transactional(readOnly = true)
    public Mono<List<Category>> validateCategoriesExist(List<String> categoryCodes) {
        return categoryRepository.findByCategoryCodeInAndDeleted(categoryCodes, false)
                .collectList()
                .flatMap(categories -> {
                    if (categories.size() != categoryCodes.size()) {
                        return Mono.error(new ProductCreateValidationException(PRODUCT_CATEGORIES_VALIDATION_FAILED));
                    }

                    return Mono.just(categories);
                });
    }

    @Transactional(readOnly = true)
    public Mono<List<Category>> findByCategoryCodeLike(String categoryCode) {
        return categoryRepository.findByCategoryCodeLike(categoryCode + CATEGORY_CODE_SUFFIX_LIKE)
                .switchIfEmpty(Mono.error(new CategoryCodeNotFoundException(CATEGORY_CODE_NOT_FOUND, categoryCode)))
                .collectList();
    }
}

package com.idolu.product.infrastructure.out.persistence.adapter;

import com.idolu.product.domain.category.Category;
import com.idolu.product.global.exception.ProductCreateValidationException;
import com.idolu.product.infrastructure.out.persistence.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.idolu.product.global.exception.ErrorCode.*;

@Component
@RequiredArgsConstructor
public class CategoryAdapter {

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Flux<Category> validateCategoriesExist(List<String> categoryCodes) {
        return categoryRepository.findByCategoryCodeIn(categoryCodes)
                .switchIfEmpty(Mono.error(new ProductCreateValidationException(PRODUCT_CATEGORIES_VALIDATION_FAILED)));
    }
}

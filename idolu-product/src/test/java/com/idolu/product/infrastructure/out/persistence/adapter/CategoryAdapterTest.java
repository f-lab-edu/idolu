package com.idolu.product.infrastructure.out.persistence.adapter;

import static com.idolu.product.global.exception.ErrorCode.PRODUCT_CATEGORIES_VALIDATION_FAILED;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.idolu.product.domain.category.Category;
import com.idolu.product.global.exception.ProductCreateValidationException;
import com.idolu.product.infrastructure.out.persistence.repository.CategoryRepository;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class CategoryAdapterTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryAdapter categoryAdapter;

    @Test
    void 카테고리가_모두_존재하면_카테고리_리스트를_반환한다() {
        // given
        Long categoryId = 1L;
        given(categoryRepository.findByCategoryIdAndDeleted(categoryId, false))
                .willReturn(Mono.just(Category.builder().build()));

        // when
        StepVerifier.create(categoryAdapter.findByCategoryId(categoryId))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void 존재하지_않는_카테고리가_있다면_카테고리_리스트를_반환한다() {
        // given
        Long categoryId = 1L;
        given(categoryRepository.findByCategoryIdAndDeleted(categoryId, false))
                .willReturn(Mono.empty());

        // when
        StepVerifier.create(categoryAdapter.findByCategoryId(categoryId))
                .verifyErrorSatisfies(e -> {
                    assertThat(e)
                            .isInstanceOf(ProductCreateValidationException.class)
                            .hasMessage(PRODUCT_CATEGORIES_VALIDATION_FAILED.getMessage());
                });
    }
}

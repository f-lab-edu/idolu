package com.idolu.product.infrastructure.out.persistence.adapter;

import com.idolu.product.domain.product.Product;
import com.idolu.product.global.exception.ProductNotFoundException;
import com.idolu.product.infrastructure.out.persistence.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static com.idolu.product.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class ProductAdapterTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductAdapter productAdapter;

    @Test
    void 상품_정보가_존재하면_해당_상품을_반환한다() {
        // given
        Product product = Product.builder().productId(1L).build();
        given(productRepository.findByProductIdAndDeleted(product.getProductId(), false))
                .willReturn(Mono.just(product));

        // when, then
        StepVerifier.create(productAdapter.findById(product))
                .expectNext(product)
                .verifyComplete();
    }

    @Test
    void 상품_정보가_존재하지_않는다면_예외를_반환한다() {
        // given
        Product product = Product.builder().productId(1L).build();
        given(productRepository.findByProductIdAndDeleted(product.getProductId(), false))
                .willReturn(Mono.empty());

        // when, then
        StepVerifier.create(productAdapter.findById(product))
                .verifyErrorSatisfies(e -> {
                    assertThat(e)
                            .isInstanceOf(ProductNotFoundException.class)
                            .hasMessage(PRODUCT_NOT_FOUND.getMessage().formatted(1L));
                });
    }
}

package com.idolu.product.application.product;

import com.idolu.product.application.product.command.ProductUpdateCommand;
import com.idolu.product.domain.product.Product;
import com.idolu.product.global.exception.ProductUpdateException;
import com.idolu.product.infrastructure.out.persistence.adapter.ProductAdapter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.List;

import static com.idolu.product.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ProductAdapter productAdapter;

    @InjectMocks
    ProductService productService;

    @Test
    void 상품_수정일이_다른_경우_예외를_반환한다() {
        // given
        ProductUpdateCommand command = createProductUpdateCommand(
                LocalDateTime.of(2025, 3, 29, 11, 11, 11));
        Product foundProduct = creteProduct(
                LocalDateTime.of(2025, 3, 29, 11, 11, 12));
        given(productAdapter.findById(anyLong()))
                .willReturn(Mono.just(foundProduct));

        // when, then
        StepVerifier.create(productService.updateProduct(command))
                .verifyErrorSatisfies(e -> {
                    assertThat(e)
                            .isInstanceOf(ProductUpdateException.class)
                            .hasMessage(PRODUCT_INVALID_UPDATED_TIME.getMessage().formatted(command.getProductId()));
                });
    }

    private static Product creteProduct(LocalDateTime updatedAt) {
        return Product.builder()
                .productId(1L)
                .updatedAt(updatedAt)
                .build();
    }

    private static ProductUpdateCommand createProductUpdateCommand(LocalDateTime updatedAt) {
        return ProductUpdateCommand.builder()
                .productId(1L)
                .contractPeriodUnitCode("MONTH")
                .servicePeriodUnitCode("MONTH")
                .productDiscountDtos(List.of())
                .productImageDtos(List.of())
                .status("ON_SALE")
                .updatedAt(updatedAt)
                .build();
    }
}

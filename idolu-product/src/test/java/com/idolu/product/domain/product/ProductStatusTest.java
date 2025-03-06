package com.idolu.product.domain.product;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static com.idolu.product.domain.product.ProductStatus.*;
import static org.assertj.core.api.Assertions.*;

class ProductStatusTest {

    @ParameterizedTest
    @MethodSource("provideInvalidState")
    void 상품_초기_상태라면_해당_상태를_반환한다(String input, ProductStatus expected) {
        ProductStatus result = validateInitialState(input);

        assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(strings = {"OFF_SALE", "COM_SOON", "SOLD_OUT", "TERMINATED"})
    void 상품_초기_상태가_아니라면_예외가_발생한다(String input) {
        assertThatThrownBy(() -> validateInitialState(input))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> provideInvalidState() {
        return Stream.of(
                Arguments.arguments("COMING_SOON", COMING_SOON),
                Arguments.arguments("ON_SALE", ON_SALE)
        );
    }
}

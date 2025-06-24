package com.idolu.idoluorder.application.order;

import com.idolu.idoluorder.application.order.command.OrderConfirmCommand;
import com.idolu.idoluorder.domain.order.OrderFailure;
import com.idolu.idoluorder.domain.order.type.OrderStatus;
import com.idolu.idoluorder.global.common.OrderException;
import com.idolu.idoluorder.global.common.ResponseCode;
import com.idolu.idoluorder.infrastructure.out.persistence.r2dbc.adapter.OrderAdapter;
import com.idolu.idoluorder.presentation.order.response.OrderConfirmationResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class OrderFailureServiceTest {

    @Mock
    OrderAdapter orderAdapter;

    @InjectMocks
    OrderFailureService orderFailureService;

    @Test
    void 이미_처리_성공한_결제_경우_성공_응답을_반환한다() {
        // given
        OrderException orderException = new OrderException(ResponseCode.ALREADY_SUCCESS_ORDER);

        OrderConfirmCommand command = OrderConfirmCommand.builder()
                .paymentKey("dummy-payment-key")
                .orderNo("dummy-order-no")
                .build();

        // when
        Mono<OrderConfirmationResponse> result = orderFailureService.handleOrderConfirmationError(orderException, command);

        // then
        StepVerifier.create(result)
                .assertNext(response -> {
                    assertThat(response.getStatus()).isEqualTo(OrderStatus.CONFIRM_SUCCESS);
                    OrderFailure failure = response.getFailure();
                    assertThat(failure.getErrorCode()).isEqualTo(ResponseCode.ALREADY_SUCCESS_ORDER.getDetailCode());
                    assertThat(failure.getMessage()).isEqualTo(ResponseCode.ALREADY_SUCCESS_ORDER.getMessage());
                })
                .verifyComplete();
    }

    @Test
    void 이미_처리_실패한_결제_경우_실패_응답을_반환한다() {
        // given
        OrderException orderException = new OrderException(ResponseCode.ALREADY_FAILURE_ORDER);

        OrderConfirmCommand command = OrderConfirmCommand.builder()
                .paymentKey("dummy-payment-key")
                .orderNo("dummy-order-no")
                .build();

        // when
        Mono<OrderConfirmationResponse> result = orderFailureService.handleOrderConfirmationError(orderException, command);

        // then
        StepVerifier.create(result)
                .assertNext(response -> {
                    assertThat(response.getStatus()).isEqualTo(OrderStatus.CONFIRM_FAILURE);
                    OrderFailure failure = response.getFailure();
                    assertThat(failure.getErrorCode()).isEqualTo(ResponseCode.ALREADY_FAILURE_ORDER.getDetailCode());
                    assertThat(failure.getMessage()).isEqualTo(ResponseCode.ALREADY_FAILURE_ORDER.getMessage());
                })
                .verifyComplete();
    }

    @Test
    void 이미_처리_성공_및_실패가_아닌_경우_상태_업데이트_후_반환한다() {
        // given
        ResponseCode errorCode = ResponseCode.INVALID_ORDER_REQUEST;
        OrderException orderException = new OrderException(errorCode);

        OrderConfirmCommand command = OrderConfirmCommand.builder()
                .paymentKey("dummy-payment-key")
                .orderNo("dummy-order-no")
                .build();

        Mockito.when(orderAdapter.finalizeOrderStatus(Mockito.any()))
                .thenReturn(Mono.just(true));

        // when, then
        StepVerifier.create(orderFailureService.handleOrderConfirmationError(orderException, command))
                .assertNext(response -> {
                    assertThat(response.getStatus()).isEqualTo(OrderStatus.CONFIRM_FAILURE);
                    assertThat(response.getFailure().getErrorCode()).isEqualTo(errorCode.getDetailCode());
                    assertThat(response.getFailure().getMessage()).isEqualTo(errorCode.getMessage());
                })
                .verifyComplete();
    }
}

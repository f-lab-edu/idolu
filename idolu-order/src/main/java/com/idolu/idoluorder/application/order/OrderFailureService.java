package com.idolu.idoluorder.application.order;

import com.idolu.idoluorder.application.order.command.OrderConfirmCommand;
import com.idolu.idoluorder.application.order.command.PaymentStatusUpdateCommand;
import com.idolu.idoluorder.domain.order.type.OrderStatus;
import com.idolu.idoluorder.domain.payment.PaymentFailure;
import com.idolu.idoluorder.global.common.OrderException;
import com.idolu.idoluorder.global.common.PaymentRequestException;
import com.idolu.idoluorder.global.common.ProductRequestException;
import com.idolu.idoluorder.global.common.ResponseCode;
import com.idolu.idoluorder.infrastructure.out.persistence.r2dbc.adapter.OrderAdapter;
import com.idolu.idoluorder.presentation.order.response.OrderConfirmationResponse;
import com.idolu.idoluorder.presentation.order.response.OrderFailure;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class OrderFailureService {

    private final OrderAdapter orderAdapter;

    public Mono<OrderConfirmationResponse> handleOrderConfirmationError(Throwable exception, OrderConfirmCommand command) {
        OrderStatus orderStatus;
        OrderFailure orderFailure;

        if (exception instanceof OrderException orderException) {
            ResponseCode errorCode = orderException.getErrorCode();

            // 이미 성공 및 실패인 경우에는 바로 응답, 아닌 경우에는 업데이트 후 응답
            if (errorCode.equals(ResponseCode.ALREADY_SUCCESS_ORDER)) {
                return Mono.just(OrderConfirmationResponse.builder()
                        .status(OrderStatus.CONFIRM_SUCCESS)
                        .failure(OrderFailure.builder()
                                .errorCode(errorCode.getDetailCode())
                                .message(errorCode.getMessage())
                                .build())
                        .build());
            } else if (errorCode.equals(ResponseCode.ALREADY_FAILURE_ORDER)) {
                return Mono.just(OrderConfirmationResponse.builder()
                        .status(OrderStatus.CONFIRM_FAILURE)
                        .failure(OrderFailure.builder()
                                .errorCode(errorCode.getDetailCode())
                                .message(errorCode.getMessage())
                                .build())
                        .build());
            } else {
                orderStatus = OrderStatus.CONFIRM_FAILURE;
                orderFailure = OrderFailure.builder()
                        .errorCode(errorCode.getDetailCode())
                        .message(exception.getMessage())
                        .build();
            }
        } else if (exception instanceof ProductRequestException productRequestException) {
            orderStatus = OrderStatus.CONFIRM_FAILURE;
            orderFailure = OrderFailure.builder()
                    .errorCode(productRequestException.getCode())
                    .message(productRequestException.getResponse())
                    .build();
        } else if (exception instanceof PaymentRequestException paymentRequestException) {
            orderStatus = paymentRequestException.toOrderStatus();
            orderFailure = OrderFailure.builder()
                    .errorCode(paymentRequestException.getErrorCode())
                    .message(paymentRequestException.getErrorMessage())
                    .build();
        } else { // TimeoutException 등의 예외는 UNKNOWN 상태로 처리
            orderStatus = OrderStatus.CONFIRM_UNKNOWN;
            orderFailure = OrderFailure.builder()
                    .errorCode(exception.getClass().getSimpleName())
                    .message(exception.getMessage())
                    .build();
        }

        return orderAdapter.updatePaymentStatus(PaymentStatusUpdateCommand.builder()
                        .paymentKey(command.getPaymentKey())
                        .orderNo(command.getOrderNo())
                        .orderStatus(orderStatus)
                        .paymentFailure(PaymentFailure.builder()
                                .errorCode(orderFailure.getErrorCode())
                                .message(orderFailure.getMessage())
                                .build())
                        .build())
                .map(result -> OrderConfirmationResponse.builder()
                        .status(orderStatus)
                        .failure(orderFailure)
                        .build());
    }
}

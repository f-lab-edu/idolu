package com.idolu.idoluorder.application.order;

import com.idolu.idoluorder.application.order.command.OrderConfirmCommand;
import com.idolu.idoluorder.application.order.command.OrderStatusUpdateCommand;
import com.idolu.idoluorder.domain.order.type.OrderStatus;
import com.idolu.idoluorder.global.common.OrderException;
import com.idolu.idoluorder.global.common.PaymentRequestException;
import com.idolu.idoluorder.global.common.ProductRequestException;
import com.idolu.idoluorder.global.common.ResponseCode;
import com.idolu.idoluorder.infrastructure.out.persistence.r2dbc.adapter.OrderAdapter;
import com.idolu.idoluorder.presentation.order.response.OrderConfirmationResponse;
import com.idolu.idoluorder.domain.order.OrderFailure;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.function.BiFunction;

@Service
@RequiredArgsConstructor
public class OrderFailureService {

    private final OrderAdapter orderAdapter;
    private final Map<Class<? extends Throwable>, BiFunction<Throwable, OrderConfirmCommand, Mono<OrderConfirmationResponse>>> handlers = Map.of(
            OrderException.class, this::handleOrderException,
            ProductRequestException.class, this::handleProductRequestException,
            PaymentRequestException.class, this::handlePaymentRequestException
    );

    public Mono<OrderConfirmationResponse> handleOrderConfirmationError(Throwable exception, OrderConfirmCommand command) {
        return handlers.entrySet().stream()
                .filter(e -> e.getKey().isAssignableFrom(exception.getClass()))
                .findFirst()
                .map(e -> e.getValue().apply(exception, command))
                .orElseGet(() -> {
                    // TimeoutException 등의 예외는 UNKNOWN 상태로 처리
                    OrderStatus orderStatus = OrderStatus.CONFIRM_UNKNOWN;
                    OrderFailure orderFailure = OrderFailure.builder()
                            .errorCode(exception.getClass().getSimpleName())
                            .message(exception.getMessage())
                            .build();

                    return orderAdapter.updateOrderStatus(OrderStatusUpdateCommand.builder()
                                    .paymentKey(command.getPaymentKey())
                                    .orderNo(command.getOrderNo())
                                    .orderStatus(orderStatus)
                                    .orderFailure(orderFailure)
                                    .build())
                            .map(result -> OrderConfirmationResponse.builder()
                                    .status(orderStatus)
                                    .failure(orderFailure)
                                    .build());
                });
    }

    private Mono<OrderConfirmationResponse> handleOrderException(Throwable exception, OrderConfirmCommand command) {
        OrderException orderException = (OrderException) exception;
        OrderStatus orderStatus;
        OrderFailure orderFailure;

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

        return orderAdapter.updateOrderStatus(OrderStatusUpdateCommand.builder()
                        .paymentKey(command.getPaymentKey())
                        .orderNo(command.getOrderNo())
                        .orderStatus(orderStatus)
                        .orderFailure(orderFailure)
                        .build())
                .map(result -> OrderConfirmationResponse.builder()
                        .status(orderStatus)
                        .failure(orderFailure)
                        .build());
    }

    private Mono<OrderConfirmationResponse> handleProductRequestException(Throwable exception, OrderConfirmCommand command) {
        ProductRequestException productRequestException = (ProductRequestException) exception;
        OrderStatus orderStatus = OrderStatus.CONFIRM_FAILURE;
        OrderFailure orderFailure = OrderFailure.builder()
                .errorCode(productRequestException.getCode())
                .message(productRequestException.getResponse())
                .build();

        return orderAdapter.updateOrderStatus(OrderStatusUpdateCommand.builder()
                        .paymentKey(command.getPaymentKey())
                        .orderNo(command.getOrderNo())
                        .orderStatus(orderStatus)
                        .orderFailure(orderFailure)
                        .productId(command.getProductId())
                        .quantity(command.getQuantity())
                        .build())
                .map(result -> OrderConfirmationResponse.builder()
                        .status(orderStatus)
                        .failure(orderFailure)
                        .build());
    }

    private Mono<OrderConfirmationResponse> handlePaymentRequestException(Throwable exception, OrderConfirmCommand command) {
        PaymentRequestException paymentRequestException = (PaymentRequestException) exception;
        OrderStatus orderStatus = paymentRequestException.toOrderStatus();
        OrderFailure orderFailure = OrderFailure.builder()
                .errorCode(paymentRequestException.getErrorCode())
                .message(paymentRequestException.getErrorMessage())
                .build();

        return orderAdapter.updateOrderStatusByPaymentRequestException(OrderStatusUpdateCommand.builder()
                        .paymentKey(command.getPaymentKey())
                        .orderNo(command.getOrderNo())
                        .orderStatus(orderStatus)
                        .orderFailure(orderFailure)
                        .productId(command.getProductId())
                        .quantity(command.getQuantity())
                        .build())
                .map(result -> OrderConfirmationResponse.builder()
                        .status(orderStatus)
                        .failure(orderFailure)
                        .build());
    }
}

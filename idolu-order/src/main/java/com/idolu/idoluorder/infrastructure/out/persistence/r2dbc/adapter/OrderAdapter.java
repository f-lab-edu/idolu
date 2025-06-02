package com.idolu.idoluorder.infrastructure.out.persistence.r2dbc.adapter;

import com.idolu.idoluorder.application.order.command.OrderConfirmCommand;
import com.idolu.idoluorder.application.order.command.PaymentStatusUpdateCommand;
import com.idolu.idoluorder.domain.order.Order;
import com.idolu.idoluorder.domain.order.OrderHistory;
import com.idolu.idoluorder.domain.order.OrderItem;
import com.idolu.idoluorder.domain.payment.PaymentEvent;
import com.idolu.idoluorder.domain.order.type.OrderStatus;
import com.idolu.idoluorder.global.common.OrderException;
import com.idolu.idoluorder.infrastructure.out.persistence.r2dbc.repository.OrderHistoryRepository;
import com.idolu.idoluorder.infrastructure.out.persistence.r2dbc.repository.OrderItemRepository;
import com.idolu.idoluorder.infrastructure.out.persistence.r2dbc.repository.OrderRepository;
import com.idolu.idoluorder.infrastructure.out.persistence.r2dbc.repository.PaymentEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import static com.idolu.idoluorder.domain.order.type.OrderStatus.EXECUTING;
import static com.idolu.idoluorder.global.common.ResponseCode.*;

@Component
@RequiredArgsConstructor
public class OrderAdapter {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderHistoryRepository orderHistoryRepository;
    private final PaymentEventRepository paymentEventRepository;

    @Transactional
    public Mono<Order> checkoutOrder(Order order) {
        return orderRepository.save(order)
                .flatMap(savedOrder -> orderItemRepository.save(savedOrder.getOrderItem().withOrderId(savedOrder.getOrderId()))
                        .thenReturn(savedOrder));
    }

    @Transactional
    public Mono<Order> updatePaymentPaymentStatusToExecuting(String orderNo, String paymentKey) {
        return checkPaymentOrderStatus(orderNo)
                .flatMap(order -> insertPaymentHistory(order, EXECUTING, "CONFIRMATION_START").thenReturn(order))
                .flatMap(order -> updateOrderStatusAndPaymentKey(order, paymentKey));
    }

    @Transactional(readOnly = true)
    public Mono<Boolean> validateOrder(Order order, OrderConfirmCommand command) {
        return orderItemRepository.findByOrderId(order.getOrderId())
                .collectList()
                .filter(orderItems -> {
                    OrderItem matchedItem = orderItems.stream()
                            .filter(orderItem -> orderItem.getProductId().equals(command.getProductId()))
                            .findFirst()
                            .orElseThrow(() -> new OrderException(INVALID_ORDER_REQUEST));

                    return matchedItem.getAmount().equals(command.getAmount()) &&
                            matchedItem.getQuantity().equals(command.getQuantity());
                })
                .switchIfEmpty(Mono.error(new OrderException(INVALID_ORDER_REQUEST)))
                .thenReturn(true);
    }

    @Transactional
    public Mono<Boolean> updatePaymentStatus(PaymentStatusUpdateCommand command) {
        return switch (command.getOrderStatus()) {
            case SUCCESS -> updatePaymentStatusToSuccess(command);
            case FAILURE -> updatePaymentStatusToFailure(command);
            case UNKNWOKN -> updatePaymentStatusToUnknown(command);
            default ->
                    Mono.error(new IllegalArgumentException("결제 상태(status: %s)는 올바르지 않습니다.".formatted(command.getOrderNo())));
        };
    }

    private Mono<Boolean> updatePaymentStatusToSuccess(PaymentStatusUpdateCommand command) {
        return orderRepository.findByOrderNo(command.getOrderNo())
                .flatMap(order -> insertPaymentHistory(order, command.getOrderStatus(), "CONFIRMATION_DONE").thenReturn(order))
                .flatMap(order -> updateOrder(order.changeStatus(command.getOrderStatus())))
                .flatMap(order -> savePaymentEvent(command, order))
                .thenReturn(true);
    }

    private Mono<Boolean> updatePaymentStatusToFailure(PaymentStatusUpdateCommand command) {
        return orderRepository.findByOrderNo(command.getOrderNo())
                .flatMap(order ->
                        insertPaymentHistory(order, command.getOrderStatus(), command.getPaymentFailure().getMessage()).thenReturn(order))
                .flatMap(order -> updateOrder(order.changeStatus(command.getOrderStatus())))
                .thenReturn(true);
    }

    private Mono<Boolean> updatePaymentStatusToUnknown(PaymentStatusUpdateCommand command) {
        return orderRepository.findByOrderNo(command.getOrderNo())
                .flatMap(order ->
                        insertPaymentHistory(order, command.getOrderStatus(), command.getPaymentFailure().getMessage()).thenReturn(order))
                .flatMap(order -> updateUnknownOrder(order.changeStatus(command.getOrderStatus())))
                .thenReturn(true);
    }

    private Mono<Order> updateUnknownOrder(Order order) {
        return orderRepository.save(order.increaseFailCount());
    }

    private Mono<PaymentEvent> savePaymentEvent(PaymentStatusUpdateCommand command, Order order) {
        return paymentEventRepository.save(PaymentEvent.builder()
                .orderId(order.getOrderId())
                .paymentKey(order.getPaymentKey())
                .method(command.getExtraDetails().getMethod())
                .paymentStatus(command.getExtraDetails().getPaymentStatus())
                .totalAmount(command.getExtraDetails().getTotalAmount())
                .balanceAmount(command.getExtraDetails().getBalanceAmount())
                .build());
    }

    private Mono<Order> updateOrder(Order order) {
        return orderRepository.save(order);
    }

    private Mono<Order> updateOrderStatusAndPaymentKey(Order order, String paymentKey) {
        return orderRepository.save(order.toExecutingWithPaymentKey(paymentKey));
    }

    private Mono<OrderHistory> insertPaymentHistory(Order order, OrderStatus orderStatus, String reason) {
        return orderHistoryRepository.save(OrderHistory.builder()
                .orderId(order.getOrderId())
                .previousStatus(order.getOrderStatus())
                .newStatus(orderStatus)
                .reason(reason)
                .build());
    }

    private Mono<Order> checkPaymentOrderStatus(String orderNo) {
        return orderRepository.findByOrderNo(orderNo)
                .switchIfEmpty(Mono.error(new OrderException(ORDER_NOT_FOUND)))
                .handle((order, sink) -> {
                    switch (order.getOrderStatus()) {
                        case NOT_STARTED, UNKNWOKN, EXECUTING -> sink.next(order);
                        case SUCCESS -> sink.error(new OrderException(ALREADY_SUCCESS_ORDER));
                        case FAILURE -> sink.error(new OrderException(ALREADY_FAILURE_ORDER));
                    }
                });
    }
}

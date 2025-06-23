package com.idolu.idoluorder.infrastructure.out.persistence.r2dbc.adapter;

import com.idolu.idoluorder.application.order.command.OrderConfirmCommand;
import com.idolu.idoluorder.application.order.command.OrderStatusUpdateCommand;
import com.idolu.idoluorder.domain.order.Order;
import com.idolu.idoluorder.domain.order.OrderEventMessagePublisher;
import com.idolu.idoluorder.domain.order.OrderHistory;
import com.idolu.idoluorder.domain.order.OrderItem;
import com.idolu.idoluorder.domain.payment.PaymentEvent;
import com.idolu.idoluorder.domain.order.type.OrderStatus;
import com.idolu.idoluorder.global.common.OrderException;
import com.idolu.idoluorder.infrastructure.out.persistence.r2dbc.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import static com.idolu.idoluorder.domain.order.type.OrderStatus.CONFIRM_EXECUTING;
import static com.idolu.idoluorder.global.common.ResponseCode.*;

@Component
@RequiredArgsConstructor
public class OrderAdapter {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderHistoryRepository orderHistoryRepository;
    private final PaymentEventRepository paymentEventRepository;
    private final OutboxAdapter outboxAdapter;
    private final OrderEventMessagePublisher eventMessagePublisher;

    @Transactional
    public Mono<Order> checkoutOrder(Order order) {
        return orderRepository.save(order)
                .flatMap(savedOrder -> orderItemRepository.save(savedOrder.getOrderItem().withOrderId(savedOrder.getOrderId()))
                        .thenReturn(savedOrder));
    }

    @Transactional
    public Mono<Order> updatePaymentPaymentStatusToExecuting(OrderConfirmCommand command) {
        return checkPaymentOrderStatus(command.getOrderNo())
                .filterWhen(order -> validateOrder(order, command))
                .flatMap(order -> insertPaymentHistory(order, CONFIRM_EXECUTING, "CONFIRMATION_START").thenReturn(order))
                .flatMap(order -> orderRepository.save(order.toExecutingWithPaymentKey(command.getPaymentKey())));
    }

    @Transactional
    public Mono<Boolean> updateOrderStatus(OrderStatusUpdateCommand command) {
        return switch (command.getOrderStatus()) {
            case CONFIRM_SUCCESS -> updateOrderStatusToSuccess(command);
            case CONFIRM_FAILURE -> updateOrderStatusToFailure(command);
            case CONFIRM_UNKNOWN -> updateOrderStatusToUnknown(command);
            default -> Mono.error(new IllegalArgumentException("결제 상태(status: %s)는 올바르지 않습니다.".formatted(command.getOrderNo())));
        };
    }

    @Transactional
    public Mono<Boolean> updateOrderStatusByPaymentRequestException(OrderStatusUpdateCommand command) {
        return updateOrderStatusToFailure(command)
                .flatMap(result -> outboxAdapter.savePaymentFailureEventMessage(command))
                .flatMap(eventMessagePublisher::publishEvent)
                .thenReturn(true);
    }

    private Mono<Boolean> updateOrderStatusToSuccess(OrderStatusUpdateCommand command) {
        return orderRepository.findByOrderNo(command.getOrderNo())
                .flatMap(order -> insertPaymentHistory(order, command.getOrderStatus(), "CONFIRMATION_DONE").thenReturn(order))
                .flatMap(order -> orderRepository.save(order.changeStatus(command.getOrderStatus())))
                .flatMap(order -> savePaymentEvent(command, order))
                .thenReturn(true);
    }

    private Mono<Boolean> updateOrderStatusToFailure(OrderStatusUpdateCommand command) {
        return orderRepository.findByOrderNo(command.getOrderNo())
                .flatMap(order ->
                        insertPaymentHistory(order, command.getOrderStatus(), command.getOrderFailure().getMessage()).thenReturn(order))
                .flatMap(order -> orderRepository.save(order.changeStatus(command.getOrderStatus())))
                .thenReturn(true);
    }

    private Mono<Boolean> updateOrderStatusToUnknown(OrderStatusUpdateCommand command) {
        return orderRepository.findByOrderNo(command.getOrderNo())
                .flatMap(order ->
                        insertPaymentHistory(order, command.getOrderStatus(), command.getOrderFailure().getMessage()).thenReturn(order))
                .flatMap(order -> orderRepository.save(order.changeStatus(command.getOrderStatus()).increaseFailCount()))
                .thenReturn(true);
    }

    private Mono<PaymentEvent> savePaymentEvent(OrderStatusUpdateCommand command, Order order) {
        return paymentEventRepository.save(PaymentEvent.builder()
                .orderId(order.getOrderId())
                .paymentKey(order.getPaymentKey())
                .method(command.getExtraDetails().getMethod())
                .paymentStatus(command.getExtraDetails().getPaymentStatus())
                .totalAmount(command.getExtraDetails().getTotalAmount())
                .balanceAmount(command.getExtraDetails().getBalanceAmount())
                .build());
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
                        case NOT_STARTED, CONFIRM_UNKNOWN, CONFIRM_EXECUTING -> sink.next(order);
                        case CONFIRM_SUCCESS -> sink.error(new OrderException(ALREADY_SUCCESS_ORDER));
                        case CONFIRM_FAILURE -> sink.error(new OrderException(ALREADY_FAILURE_ORDER));
                    }
                });
    }

    private Mono<Boolean> validateOrder(Order order, OrderConfirmCommand command) {
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
}

package com.idolu.idoluorder.infrastructure.out.persistence.r2dbc.adapter;

import com.idolu.idoluorder.domain.order.Order;
import com.idolu.idoluorder.domain.order.OrderHistory;
import com.idolu.idoluorder.domain.order.type.OrderStatus;
import com.idolu.idoluorder.global.common.OrderException;
import com.idolu.idoluorder.infrastructure.out.persistence.r2dbc.repository.OrderHistoryRepository;
import com.idolu.idoluorder.infrastructure.out.persistence.r2dbc.repository.OrderItemRepository;
import com.idolu.idoluorder.infrastructure.out.persistence.r2dbc.repository.OrderRepository;
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

    @Transactional
    public Mono<Order> checkoutOrder(Order order) {
        return orderRepository.save(order)
                .flatMap(savedOrder -> orderItemRepository.save(savedOrder.getOrderItem().withOrderId(savedOrder.getOrderId()))
                        .thenReturn(savedOrder));
    }

    @Transactional
    public Mono<Order> updatePaymentPaymentStatusToExecuting(Long orderId, String paymentKey) {
        return checkPaymentOrderStatus(orderId)
                .flatMap(order -> insertPaymentHistory(order, EXECUTING, "CONFIRMATION_START").thenReturn(order))
                .flatMap(order -> updateOrderStatusAndPaymentKey(order, paymentKey));
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

    private Mono<Order> checkPaymentOrderStatus(Long orderId) {
        return orderRepository.findById(orderId)
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

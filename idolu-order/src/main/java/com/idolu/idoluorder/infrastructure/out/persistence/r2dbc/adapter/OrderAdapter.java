package com.idolu.idoluorder.infrastructure.out.persistence.r2dbc.adapter;

import com.idolu.idoluorder.domain.order.Order;
import com.idolu.idoluorder.infrastructure.out.persistence.r2dbc.repository.OrderItemRepository;
import com.idolu.idoluorder.infrastructure.out.persistence.r2dbc.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class OrderAdapter {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Transactional
    public Mono<Order> checkoutOrder(Order order) {
        return orderRepository.save(order)
                .flatMap(savedOrder -> orderItemRepository.save(savedOrder.getOrderItem().withOrderId(savedOrder.getOrderId()))
                        .then(Mono.just(savedOrder)));
    }
}

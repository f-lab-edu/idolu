package com.idolu.idoluorder.application.order;

import com.idolu.idoluorder.application.order.command.CheckoutCommand;
import com.idolu.idoluorder.domain.order.Order;
import com.idolu.idoluorder.domain.order.OrderItem;
import com.idolu.idoluorder.domain.order.type.OrderStatus;
import com.idolu.idoluorder.infrastructure.out.persistence.r2dbc.adapter.OrderAdapter;
import com.idolu.idoluorder.infrastructure.out.web.ProductAdapter;
import com.idolu.idoluorder.infrastructure.out.web.UserAdapter;
import com.idolu.idoluorder.infrastructure.out.web.response.ProductDetailResponse;
import com.idolu.idoluorder.presentation.order.response.CheckoutResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.function.TupleUtils;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserAdapter userAdapter;
    private final ProductAdapter productAdapter;
    private final OrderAdapter orderAdapter;

    public Mono<CheckoutResponse> checkout(CheckoutCommand command, String authorization) {
        return Mono.zip(
                        userAdapter.validateAccessToken(authorization),
                        productAdapter.getProductInformation(command.getProductId()))
                .flatMap(TupleUtils.function((userId, product) ->
                        orderAdapter.checkoutOrder(createOrder(userId, command).withOrderItem(createOrderItem(product, command)))))
                .map(order -> CheckoutResponse.builder()
                        .orderId(order.getOrderId())
                        .orderNo(order.getOrderNo())
                        .amount(order.getOrderItem().getAmount())
                        .build());
    }

    private OrderItem createOrderItem(ProductDetailResponse product, CheckoutCommand command) {
        return OrderItem.builder()
                .productId(product.getProductId())
                .productName(product.getName())
                .basicPrice(product.getPrice().getBasicPrice())
                .sellingPrice(product.getPrice().getSellingPrice())
                .discountRate(product.getPrice().getDiscountRate())
                .quantity(command.getQuantity())
                .amount(product.getPrice().getSellingPrice().multiply(BigDecimal.valueOf(command.getQuantity())))
                .build();
    }

    private Order createOrder(Long userId, CheckoutCommand command) {
        return Order.builder()
                .buyerId(userId)
                .orderNo(UUID.randomUUID().toString())
                .recipient(command.getRecipient())
                .phone(command.getPhone())
                .zipCode(command.getZipCode())
                .baseAddress(command.getBaseAddress())
                .detailAddress(command.getDetailAddress())
                .orderStatus(OrderStatus.NOT_STARTED)
                .build();
    }
}

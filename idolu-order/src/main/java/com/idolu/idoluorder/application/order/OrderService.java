package com.idolu.idoluorder.application.order;

import com.idolu.idoluorder.application.order.command.CheckoutCommand;
import com.idolu.idoluorder.application.order.command.OrderConfirmCommand;
import com.idolu.idoluorder.application.order.command.OrderStatusUpdateCommand;
import com.idolu.idoluorder.domain.order.Order;
import com.idolu.idoluorder.domain.order.OrderItem;
import com.idolu.idoluorder.domain.order.type.OrderStatus;
import com.idolu.idoluorder.infrastructure.out.persistence.r2dbc.adapter.OrderAdapter;
import com.idolu.idoluorder.infrastructure.out.web.PaymentExecutorAdapter;
import com.idolu.idoluorder.infrastructure.out.web.ProductAdapter;
import com.idolu.idoluorder.infrastructure.out.web.request.ProductStockUpdateRequest;
import com.idolu.idoluorder.infrastructure.out.web.response.ProductDetailResponse;
import com.idolu.idoluorder.presentation.order.response.CheckoutResponse;
import com.idolu.idoluorder.presentation.order.response.OrderConfirmationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductAdapter productAdapter;
    private final OrderAdapter orderAdapter;
    private final PaymentExecutorAdapter paymentExecutorAdapter;
    private final OrderFailureService orderFailureService;

    public Mono<CheckoutResponse> checkout(CheckoutCommand command) {
        return productAdapter.getProductInformation(command.getProductId())
                .flatMap(product ->
                        orderAdapter.checkoutOrder(createOrder(command).withOrderItem(createOrderItem(product, command))))
                .map(order -> CheckoutResponse.builder()
                        .orderId(order.getOrderId())
                        .orderNo(order.getOrderNo())
                        .amount(order.getOrderItem().getAmount())
                        .build());
    }

    public Mono<OrderConfirmationResponse> confirm(OrderConfirmCommand command) {
        return orderAdapter.updatePaymentPaymentStatusToExecuting(command)
                .filterWhen(order -> productAdapter.decreaseProductStock(ProductStockUpdateRequest.builder()
                        .orderNo(command.getOrderNo())
                        .productId(command.getProductId())
                        .stock(command.getQuantity())
                        .stockType("DECREASE")
                        .build()))
                .flatMap(order -> orderAdapter.updateOrderStatus(order, OrderStatus.CONFIRM_PAYMENT_EXECUTING, "CONFIRMATION_PAYMENT_START"))
                .flatMap(order -> paymentExecutorAdapter.execute(command))
                .flatMap(paymentExecutionResult ->
                        orderAdapter.finalizeOrderStatus(OrderStatusUpdateCommand.builder()
                                        .paymentKey(paymentExecutionResult.getPaymentKey())
                                        .orderNo(paymentExecutionResult.getOrderNo())
                                        .orderStatus(paymentExecutionResult.toOrderStatus())
                                        .extraDetails(paymentExecutionResult.getExtraDetails())
                                        .build())
                                .thenReturn(paymentExecutionResult))
                .map(paymentExecutionResult -> OrderConfirmationResponse.builder()
                        .status(paymentExecutionResult.toOrderStatus())
                        .build())
                .onErrorResume(error -> orderFailureService.handleOrderConfirmationError(error, command));
    }

    private OrderItem createOrderItem(ProductDetailResponse product, CheckoutCommand command) {
        return OrderItem.builder()
                .productId(product.getProductId())
                .productName(product.getName())
                .basicPrice(product.getPrice().getBasicPrice())
                .sellingPrice(product.getPrice().getSellingPrice())
                .discountRate(product.getPrice().getDiscountRate())
                .quantity(command.getQuantity())
                .amount(IntStream.range(0, command.getQuantity())
                        .mapToObj(i -> product.getPrice().getSellingPrice())
                        .reduce(BigDecimal.ZERO, BigDecimal::add))
                .build();
    }

    private Order createOrder(CheckoutCommand command) {
        return Order.builder()
                .buyerId(command.getUserId())
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

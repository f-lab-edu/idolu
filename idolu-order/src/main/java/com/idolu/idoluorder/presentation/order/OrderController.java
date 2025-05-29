package com.idolu.idoluorder.presentation.order;

import com.idolu.idoluorder.application.order.OrderService;
import com.idolu.idoluorder.domain.order.Order;
import com.idolu.idoluorder.global.common.ApiResponse;
import com.idolu.idoluorder.presentation.order.request.CheckoutRequest;
import com.idolu.idoluorder.presentation.order.request.OrderConfirmRequest;
import com.idolu.idoluorder.presentation.order.response.CheckoutResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/checkout")
    public Mono<ApiResponse<CheckoutResponse>> checkout(
            @RequestBody CheckoutRequest request,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {

        return orderService.checkout(request.toCommand(), authorization)
                .map(ApiResponse::ok);
    }

    @PostMapping("/confirm")
    public Mono<Order> confirm(
            @RequestBody OrderConfirmRequest request,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {

        return orderService.confirm(request.toCommand(), authorization);
    }
}

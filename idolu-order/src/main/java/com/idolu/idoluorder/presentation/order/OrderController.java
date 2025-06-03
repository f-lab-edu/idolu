package com.idolu.idoluorder.presentation.order;

import com.idolu.idoluorder.application.order.OrderService;
import com.idolu.idoluorder.domain.payment.PaymentExecutionResult;
import com.idolu.idoluorder.global.common.ApiResponse;
import com.idolu.idoluorder.presentation.order.request.CheckoutRequest;
import com.idolu.idoluorder.presentation.order.request.OrderConfirmRequest;
import com.idolu.idoluorder.presentation.order.response.CheckoutResponse;
import com.idolu.idoluorder.presentation.order.response.OrderConfirmationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/checkout")
    public Mono<ApiResponse<CheckoutResponse>> checkout(@RequestBody CheckoutRequest request) {

        return Mono.deferContextual(ctx -> {
            Long userId = ctx.get("userId");
            return orderService.checkout(request.toCommand(userId));
        }).map(ApiResponse::ok);
    }

    @PostMapping("/confirm")
    public Mono<OrderConfirmationResponse> confirm(
            @RequestBody OrderConfirmRequest request) {

        return orderService.confirm(request.toCommand());
    }
}

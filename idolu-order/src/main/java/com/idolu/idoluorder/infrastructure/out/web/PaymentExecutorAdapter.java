package com.idolu.idoluorder.infrastructure.out.web;

import com.idolu.idoluorder.application.order.command.OrderConfirmCommand;
import com.idolu.idoluorder.domain.payment.PaymentExtraDetails;
import com.idolu.idoluorder.global.common.PaymentRequestException;
import com.idolu.idoluorder.infrastructure.out.web.request.PaymentExecutionRequest;
import com.idolu.idoluorder.domain.payment.PaymentExecutionResult;
import com.idolu.idoluorder.infrastructure.out.web.response.TossPaymentConfirmationResponse;
import com.idolu.idoluorder.infrastructure.out.web.response.TossPaymentError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.idolu.idoluorder.domain.payment.type.PaymentStatus.*;
import static com.idolu.idoluorder.domain.payment.type.PaymentMethod.toPaymentMethod;
import static com.idolu.idoluorder.domain.payment.type.PaymentType.*;
import static com.idolu.idoluorder.infrastructure.out.web.response.TossPaymentError.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentExecutorAdapter {

    private final WebClient tossWebClient;

    public Mono<PaymentExecutionResult> execute(OrderConfirmCommand command) {
        return tossWebClient.post()
                .uri("/v1/payments/confirm")
                .header("Idempotency-Key", command.getOrderNo())
                .bodyValue(PaymentExecutionRequest.builder()
                        .paymentKey(command.getPaymentKey())
                        .orderId(command.getOrderNo())
                        .amount(command.getAmount().intValue())
                        .build())
                .retrieve()
                .onStatus(statusCode -> statusCode.is4xxClientError() || statusCode.is5xxServerError(), this::createTossPaymentException)
                .bodyToMono(TossPaymentConfirmationResponse.class)
                .map(response -> PaymentExecutionResult.builder()
                        .paymentKey(command.getPaymentKey())
                        .orderNo(command.getOrderNo())
                        .isSuccess(true)
                        .isFailure(false)
                        .isUnknown(false)
                        .isRetryable(false)
                        .extraDetails(PaymentExtraDetails.builder()
                                .type(toPaymentType(response.getType()))
                                .method(toPaymentMethod(response.getMethod()))
                                .approvedAt(LocalDateTime.parse(response.getApprovedAt(), DateTimeFormatter.ISO_OFFSET_DATE_TIME)) // yyyy-MM-dd'T'HH:mm:ss±hh:mm ISO 8601 형식
                                .orderName(response.getOrderName())
                                .paymentStatus(toPaymentStatus(response.getStatus()))
                                .totalAmount(response.getTotalAmount())
                                .balanceAmount(response.getBalanceAmount())
                                .build())
                        .build())
                .retryWhen(Retry.backoff(2, Duration.ofSeconds(1)).jitter(0.1) // 재시도
                        .filter(ex -> ex instanceof PaymentRequestException && ((PaymentRequestException) ex).getIsUnknown())
                        .doBeforeRetry(retrySignal -> log.info("retryCount: {}, errorCode: {}", retrySignal.totalRetries(), retrySignal.failure().toString()))
                        .onRetryExhaustedThrow((spec, retrySignal) -> retrySignal.failure())); // 재시도 모두 소진 시 발생한 예외 그대로 전달
    }

    private Mono<PaymentRequestException> createTossPaymentException(ClientResponse clientResponse) {
        return clientResponse.bodyToMono(TossPaymentConfirmationResponse.TossFailureResponse.class)
                .flatMap(response -> {
                    TossPaymentError tossPaymentError = toTossPaymentError(response.getCode());
                    return Mono.error(PaymentRequestException.builder()
                            .errorCode(tossPaymentError.name())
                            .errorMessage(tossPaymentError.getDescription())
                                    .isSuccess(tossPaymentError.isSuccess())
                                    .isFailure(tossPaymentError.isFailure())
                                    .isUnknown(tossPaymentError.isUnknown())
                            .build());
                });
    }
}

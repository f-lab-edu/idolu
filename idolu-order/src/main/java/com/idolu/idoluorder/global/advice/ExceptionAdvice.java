package com.idolu.idoluorder.global.advice;

import com.idolu.idoluorder.global.common.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(Exception.class)
    protected Mono<ApiResponse<Void>> handleUnknown(Exception exception) {
        log.warn("Exception: {}", exception.getMessage());
        return Mono.just(ApiResponse.error(ResponseCode.SERVER_ERROR)) ;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected Mono<ApiResponse<Void>> illegalArgumentException(IllegalArgumentException exception) {
        log.warn("IllegalArgumentException: {}", exception.getMessage());
        return Mono.just(ApiResponse.error(ResponseCode.INVALID_REQUEST)) ;
    }

    @ExceptionHandler(WebExchangeBindException.class)
    protected Mono<ApiResponse<Void>> bindException(WebExchangeBindException exception) {
        log.warn("WebExchangeBindException: {}", exception.getMessage());
        return Mono.just(ApiResponse.error(ResponseCode.INVALID_REQUEST));
    }

    @ExceptionHandler(OrderException.class)
    protected Mono<ApiResponse<Void>> orderException(OrderException exception) {
        log.warn("OrderException: {}", exception.getMessage());
        return Mono.just(ApiResponse.error(exception.getErrorCode()));
    }

    @ExceptionHandler(ProductRequestException.class)
    protected Mono<ApiResponse<Void>> productRequestException(ProductRequestException exception) {
        log.warn("ProductRequestException: {}", exception.getResponse());
        return Mono.just(ApiResponse.error(exception.getCode(), exception.getResponse()));
    }
}

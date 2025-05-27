package com.idolu.product.global.advice;

import com.idolu.product.global.common.ApiResponse;

import com.idolu.product.global.common.ProductException;
import com.idolu.product.global.common.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(Exception.class)
    protected Mono<ApiResponse<Void>> handleUnknown(Exception exception) {
        log.error("Exception: {}", exception.getMessage());
        return Mono.just(ApiResponse.error(ResponseCode.SERVER_ERROR)) ;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected Mono<ApiResponse<String>> illegalArgumentException(IllegalArgumentException exception) {
        log.warn("IllegalArgumentException: {}", exception.getMessage());
        return Mono.just(ApiResponse.error(ResponseCode.INVALID_REQUEST)) ;
    }

    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected Mono<ApiResponse<String>> bindException(WebExchangeBindException exception) {
        log.warn("WebExchangeBindException: {}", exception.getMessage());
        return Mono.just(ApiResponse.error(ResponseCode.INVALID_REQUEST));
    }

    @ExceptionHandler(ProductException.class)
    protected Mono<ApiResponse<Void>> productException(ProductException exception) {
        log.warn("ProductException: {}", exception.getMessage());
        return Mono.just(ApiResponse.error(exception.getErrorCode()));
    }
}

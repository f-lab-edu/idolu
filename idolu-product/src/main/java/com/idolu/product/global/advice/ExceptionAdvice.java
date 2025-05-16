package com.idolu.product.global.advice;

import com.idolu.product.domain.product.ProductImage;
import com.idolu.product.global.common.ApiResponse;
import com.idolu.product.global.common.DetailErrorCodeResponse;
import com.idolu.product.global.exception.ProductCreateValidationException;

import com.idolu.product.global.exception.ProductImageNotFoundException;
import com.idolu.product.global.exception.ProductUpdateException;
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

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected Mono<ApiResponse<String>> illegalArgumentException(IllegalArgumentException exception) {
        log.warn("IllegalArgumentException: {}", exception.getMessage());
        return Mono.just(ApiResponse.of(HttpStatus.BAD_REQUEST, exception.getMessage(), null)) ;
    }

    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected Mono<ApiResponse<String>> bindException(WebExchangeBindException exception) {
        log.warn("WebExchangeBindException: {}", exception.getMessage());
        return Mono.just(ApiResponse.of(
                HttpStatus.BAD_REQUEST,
                exception.getBindingResult().getAllErrors().get(0).getDefaultMessage(),
                null
        ));
    }

    @ExceptionHandler(ProductCreateValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected Mono<ApiResponse<DetailErrorCodeResponse>> productCreateValidationException(ProductCreateValidationException exception) {
        log.warn("ProductCreateValidationException: {}", exception.getMessage());
        return Mono.just(ApiResponse.of(
            exception.getErrorCode().getStatus(),
            null,
            DetailErrorCodeResponse.from(exception.getErrorCode().getDetailCode())
        ));
    }

    @ExceptionHandler(ProductUpdateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected Mono<ApiResponse<DetailErrorCodeResponse>> productUpdateException(ProductUpdateException exception) {
        log.warn("ProductUpdateException: {}", exception.getMessage());
        return Mono.just(ApiResponse.of(
                exception.getErrorCode().getStatus(),
                null,
                DetailErrorCodeResponse.from(exception.getErrorCode().getDetailCode())
        ));
    }

    @ExceptionHandler(ProductImageNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected Mono<ApiResponse<DetailErrorCodeResponse>> productImageNotFoundException(ProductImageNotFoundException exception) {
        log.warn("ProductImageNotFoundException: {}", exception.getMessage());
        return Mono.just(ApiResponse.of(
                exception.getErrorCode().getStatus(),
                null,
                DetailErrorCodeResponse.from(exception.getErrorCode().getDetailCode())
        ));
    }
}

package com.idolu.user.global.advice;

import com.idolu.user.global.common.ApiResponse;
import com.idolu.user.global.common.DetailErrorCodeResponse;
import com.idolu.user.global.exception.ResponseCode;
import com.idolu.user.global.exception.UserException;
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

    @ExceptionHandler(UserException.class)
    protected Mono<ApiResponse<DetailErrorCodeResponse>> userException(UserException exception) {
        log.warn("UserAlreadyExistException: {}", exception.getMessage());
        return Mono.just(ApiResponse.error(exception.getErrorCode()));
    }
}

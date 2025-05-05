package com.idolu.user.global.advice;

import com.idolu.user.global.common.ApiResponse;
import com.idolu.user.global.common.DetailErrorCodeResponse;
import com.idolu.user.global.exception.UserAlreadyExistException;
import com.idolu.user.global.exception.UserNotFoundException;
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

    @ExceptionHandler(UserAlreadyExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected Mono<ApiResponse<DetailErrorCodeResponse>> userAlreadyExistException(UserAlreadyExistException exception) {
        log.warn("UserAlreadyExistException: {}", exception.getMessage());
        return Mono.just(ApiResponse.of(
                exception.getErrorCode().getStatus(),
                null,
                DetailErrorCodeResponse.from(exception.getErrorCode().getDetailCode())
        ));
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected Mono<ApiResponse<DetailErrorCodeResponse>> userNotFoundException(UserNotFoundException exception) {
        log.warn("UserNotFoundException: {}", exception.getMessage());
        return Mono.just(ApiResponse.of(
                exception.getErrorCode().getStatus(),
                null,
                DetailErrorCodeResponse.from(exception.getErrorCode().getDetailCode())
        ));
    }
}

package com.idolu.product.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    PRODUCT_CATEGORIES_VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "PRODUCT-001", "카테고리 정보가 적절하지 못합니다."),
    PRODUCT_STATUS_VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "PRODUCT-002", "상품 초기 상태가 적절하지 못합니다."),
    PRODUCT_NOT_FOUND(HttpStatus.BAD_REQUEST, "PRODUCT-003", "상품 정보를 조회할 수 없습니다. 상품 번호: %d"),
    PRODUCT_INVALID_UPDATED_TIME(HttpStatus.BAD_REQUEST, "PRODUCT-004", "상품 마지막 수정일이 일치하지 않습니다."),

    ENUM_VALIDATE_FAILED(HttpStatus.BAD_REQUEST, "COMMON-001", "변환할 타입이 없습니다. value: %s"),
    ;

    private final HttpStatus status;
    private final String detailCode;
    private final String message;

}

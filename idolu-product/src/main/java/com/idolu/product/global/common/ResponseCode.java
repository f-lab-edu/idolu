package com.idolu.product.global.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ResponseCode {

    SUCCESS("PRODUCT-0000", "요청 성공"),
    INVALID_REQUEST("PRODUCT-0001", "잘못된 요청"),
    PRODUCT_CATEGORIES_VALIDATION_FAILED("PRODUCT-0002", "잘못된 카테고리 요청"),
    PRODUCT_STATUS_VALIDATION_FAILED("PRODUCT-0003", "잘못된 상품 초기 상태"),
    PRODUCT_NOT_FOUND("PRODUCT-0004", "잘못된 상품 정보 요청"),
    PRODUCT_INVALID_UPDATED_TIME("PRODUCT-0005", "상품 마지막 수정일 미일치"),
    PRODUCT_DUPLICATED_REQUEST("PRODUCT_0006", "상품 변경 충돌"),
    PRODUCT_INSUFFICIENT_STOCK("PRODUCT-0007", "상품 재고 부족"),
    PRODUCT_IMAGE_NOT_FOUND("PRODUCT-0008", "잘못된 상품 이미지 정보 조회"),
    STORE_NOT_FOUND("PRODUCT-0009", "잘못된 회원사 정보 조회"),
    SERVER_ERROR("PRODUCT-1999", "서버 오류"),
    ;

    private final String detailCode;
    private final String message;

}

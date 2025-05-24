package com.idolu.idoluorder.global.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseCode {

    SUCCESS("ORDER-0000", "요청 성공"),
    INVALID_REQUEST("ORDER-0001", "잘못된 요청"),
    INVALID_ACCESS_TOKEN("ORDER-0002", "유효하지 않는 토큰"),
    PRODUCT_NOT_FOUND("ORDER-0003", "잘못된 상품 정보 요청"),
    SERVER_ERROR("ORDER-1999", "서버 오류"),
    ;

    private final String detailCode;
    private final String message;
}

package com.idolu.product.global.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 공통적으로 사용하는 응답 포맷
 */
@Getter
@NoArgsConstructor
public class ApiResponse<T> {

    private String code;
    private String message;
    private T data;

    public ApiResponse(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ApiResponse(ResponseCode code, T data) {
        this.code = code.getDetailCode();
        this.message = code.getMessage();
        this.data = data;
    }

    public static <T> ApiResponse<T> of(ResponseCode code, T data) {
        return new ApiResponse<>(code, data);
    }

    public static <T> ApiResponse<T> ok(T data) {
        return of(ResponseCode.SUCCESS, data);
    }

    public static <T> ApiResponse<T> error(ResponseCode code) {
        return of(code, null);
    }
}

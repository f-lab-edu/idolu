package com.idolu.user.global.common;

import com.idolu.user.global.exception.ResponseCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 공통적으로 사용하는 응답 포맷
 */
@Getter
public class ApiResponse<T> {

    private String code;
    private String message;
    private T data;

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

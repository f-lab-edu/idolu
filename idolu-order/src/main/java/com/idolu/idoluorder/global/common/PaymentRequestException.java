package com.idolu.idoluorder.global.common;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentRequestException extends RuntimeException {

    private String errorCode;
    private String errorMessage;
    private Boolean isSuccess;
    private Boolean isFailure;
    private Boolean isUnknown;
}

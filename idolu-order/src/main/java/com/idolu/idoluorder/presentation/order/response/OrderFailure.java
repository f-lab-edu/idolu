package com.idolu.idoluorder.presentation.order.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderFailure {

    private String errorCode;
    private String message;
}

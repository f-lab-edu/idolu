package com.idolu.idoluorder.domain.order;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderFailure {

    private String errorCode;
    private String message;
}

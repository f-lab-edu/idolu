package com.idolu.idoluorder.presentation.order.request;

import com.idolu.idoluorder.application.order.command.CheckoutCommand;
import lombok.Getter;

@Getter
public class CheckoutRequest {

    private Long productId;

    private Integer quantity;

    private String recipient;

    private String phone;

    private String zipCode;

    private String baseAddress;

    private String detailAddress;

    public CheckoutCommand toCommand(Long userId) {
        return CheckoutCommand.builder()
                .userId(userId)
                .productId(this.productId)
                .quantity(this.quantity)
                .recipient(this.recipient)
                .phone(this.phone)
                .zipCode(this.zipCode)
                .baseAddress(this.baseAddress)
                .detailAddress(this.detailAddress)
                .build();
    }
}

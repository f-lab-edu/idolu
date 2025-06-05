package com.idolu.idoluorder.application.order.command;

import lombok.Builder;
import lombok.Getter;


@Getter
public class CheckoutCommand {

    private Long userId;

    private Long productId;

    private Integer quantity;

    private String recipient;

    private String phone;

    private String zipCode;

    private String baseAddress;

    private String detailAddress;

    @Builder
    public CheckoutCommand(Long userId, Long productId, Integer quantity, String recipient, String phone, String zipCode, String baseAddress, String detailAddress) {
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.recipient = recipient;
        this.phone = phone;
        this.zipCode = zipCode;
        this.baseAddress = baseAddress;
        this.detailAddress = detailAddress;
    }
}

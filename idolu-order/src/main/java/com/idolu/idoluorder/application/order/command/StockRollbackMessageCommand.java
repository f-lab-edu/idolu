package com.idolu.idoluorder.application.order.command;

import com.idolu.idoluorder.domain.outbox.type.MessageType;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class StockRollbackMessageCommand {

    private MessageType type;
    private String topic;
    private String key;
    private String payload;

    @Builder
    @Getter
    public static class Payload {

        private String orderNo;
        private Long productId;
        private Integer quantity;

        public static Payload from(OrderStatusUpdateCommand command) {
            return Payload.builder()
                    .orderNo(command.getOrderNo())
                    .productId(command.getProductId())
                    .quantity(command.getQuantity())
                    .build();
        }
    }
}

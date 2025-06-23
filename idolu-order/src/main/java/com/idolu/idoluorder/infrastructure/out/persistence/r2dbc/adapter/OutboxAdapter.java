package com.idolu.idoluorder.infrastructure.out.persistence.r2dbc.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.idolu.idoluorder.application.order.command.OrderStatusUpdateCommand;
import com.idolu.idoluorder.application.order.command.StockRollbackMessageCommand.Payload;
import com.idolu.idoluorder.domain.outbox.Outbox;
import com.idolu.idoluorder.application.order.command.StockRollbackMessageCommand;
import com.idolu.idoluorder.domain.outbox.type.MessageStatus;
import com.idolu.idoluorder.domain.outbox.type.MessageType;
import com.idolu.idoluorder.infrastructure.out.persistence.r2dbc.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static com.idolu.idoluorder.global.constants.Topic.PRODUCT_STOCK_ROLLBACK_TOPIC;

@Component
@RequiredArgsConstructor
public class OutboxAdapter {

    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    public Mono<StockRollbackMessageCommand> savePaymentFailureEventMessage(OrderStatusUpdateCommand command) {
        StockRollbackMessageCommand stockRollbackMessageCommand = createStockRollbackMessageCommand(command);

        return outboxRepository.save(Outbox.builder()
                        .type(stockRollbackMessageCommand.getType())
                        .topic(stockRollbackMessageCommand.getTopic())
                        .topicKey(stockRollbackMessageCommand.getKey())
                        .payload(stockRollbackMessageCommand.getPayload())
                        .status(MessageStatus.INIT)
                        .build())
                .thenReturn(stockRollbackMessageCommand);
    }

    private StockRollbackMessageCommand createStockRollbackMessageCommand(OrderStatusUpdateCommand command) {
        return StockRollbackMessageCommand.builder()
                .type(MessageType.PAYMENT_CONFIRMATION_FAILURE)
                .key(command.getOrderNo())
                .topic(PRODUCT_STOCK_ROLLBACK_TOPIC)
                .payload(createPayload(command))
                .build();
    }

    private String createPayload(OrderStatusUpdateCommand command) {
        try {
            return objectMapper.writeValueAsString(Payload.from(command));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public Mono<Boolean> updateMessageStatus(StockRollbackMessageCommand command, MessageStatus messageStatus) {
        return outboxRepository.findOutboxByTopicKeyAndType(command.getKey(), command.getType())
                .flatMap(outbox -> outboxRepository.save(outbox.updateMessageStatus(messageStatus)))
                .thenReturn(true);
    }
}

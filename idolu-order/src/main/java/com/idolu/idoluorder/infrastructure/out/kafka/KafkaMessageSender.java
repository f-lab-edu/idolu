package com.idolu.idoluorder.infrastructure.out.kafka;

import com.idolu.idoluorder.application.order.command.StockRollbackMessageCommand;
import com.idolu.idoluorder.domain.outbox.type.MessageStatus;
import com.idolu.idoluorder.infrastructure.out.persistence.r2dbc.adapter.OutboxAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaMessageSender {

    private final ReactiveKafkaProducerTemplate<String, String> kafkaProducer;
    private final OutboxAdapter outboxAdapter;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendMessageAfterCommit(StockRollbackMessageCommand command) {
        dispatchStockRollbackMessage(command)
                .onErrorContinue((e, o) -> log.error("sendMessageEvent: {}", e.toString()))
                .subscribe();
    }

    public Mono<Boolean> dispatchStockRollbackMessage(StockRollbackMessageCommand command) {
        return kafkaProducer.send(
                        command.getTopic(),
                        command.getKey(),
                        command.getPayload())
                .flatMap(senderResult -> {
                    if (senderResult.exception() != null) {
                        return outboxAdapter.updateMessageStatus(command, MessageStatus.SUCCESS);
                    }

                    return outboxAdapter.updateMessageStatus(command, MessageStatus.SUCCESS);
                });
    }
}

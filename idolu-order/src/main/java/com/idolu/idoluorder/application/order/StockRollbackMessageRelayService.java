package com.idolu.idoluorder.application.order;

import com.idolu.idoluorder.application.order.command.StockRollbackMessageCommand;
import com.idolu.idoluorder.infrastructure.out.kafka.KafkaMessageSender;
import com.idolu.idoluorder.infrastructure.out.persistence.r2dbc.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.idolu.idoluorder.domain.outbox.type.MessageStatus.*;
import static com.idolu.idoluorder.domain.outbox.type.MessageType.PAYMENT_CONFIRMATION_FAILURE;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockRollbackMessageRelayService {

    private final OutboxRepository outboxRepository;
    private final KafkaMessageSender messageSender;

    @Scheduled(fixedDelay = 60, initialDelay = 60, timeUnit = TimeUnit.SECONDS)
    public void relay() {
        outboxRepository.findOutboxByTypeAndStatusInOrderByOutboxIdAsc(PAYMENT_CONFIRMATION_FAILURE, List.of(INIT, FAILURE))
                .doOnNext(outbox -> log.info("send stock rollback message. outbox id: {}", outbox.getOutboxId()))
                .flatMap(outbox -> messageSender.dispatchStockRollbackMessage(StockRollbackMessageCommand.builder()
                        .type(outbox.getType())
                        .topic(outbox.getTopic())
                        .key(outbox.getTopicKey())
                        .payload(outbox.getPayload())
                        .build()))
                .subscribeOn(Schedulers.newSingle("stock-rollback-message-relay"))
                .subscribe();
    }
}

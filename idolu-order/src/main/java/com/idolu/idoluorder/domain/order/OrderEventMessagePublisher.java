package com.idolu.idoluorder.domain.order;

import com.idolu.idoluorder.application.order.command.StockRollbackMessageCommand;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalEventPublisher;
import reactor.core.publisher.Mono;

@Service
public class OrderEventMessagePublisher {

    private final ApplicationEventPublisher eventPublisher;
    private final TransactionalEventPublisher transactionalEventPublisher;

    public OrderEventMessagePublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
        this.transactionalEventPublisher = new TransactionalEventPublisher(eventPublisher);
    }

    public Mono<StockRollbackMessageCommand> publishEvent(StockRollbackMessageCommand command) {
        return transactionalEventPublisher.publishEvent(command)
                .thenReturn(command);
    }
}

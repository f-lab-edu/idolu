package com.idolu.idoluorder.infrastructure.out.persistence.r2dbc.repository;

import com.idolu.idoluorder.domain.outbox.Outbox;
import com.idolu.idoluorder.domain.outbox.type.MessageStatus;
import com.idolu.idoluorder.domain.outbox.type.MessageType;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;

public interface OutboxRepository extends R2dbcRepository<Outbox, Long> {

    Mono<Outbox> findOutboxByTopicKeyAndType(String topicKey, MessageType type);

    Flux<Outbox> findOutboxByTypeAndStatusInOrderByOutboxIdAsc(MessageType type, Collection<MessageStatus> statuses);
}

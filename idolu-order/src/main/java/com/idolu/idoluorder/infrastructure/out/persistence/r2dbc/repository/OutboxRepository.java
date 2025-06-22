package com.idolu.idoluorder.infrastructure.out.persistence.r2dbc.repository;

import com.idolu.idoluorder.domain.outbox.Outbox;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface OutboxRepository extends R2dbcRepository<Outbox, Long> {
}

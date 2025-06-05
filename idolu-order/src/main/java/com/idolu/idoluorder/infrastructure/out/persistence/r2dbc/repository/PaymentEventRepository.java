package com.idolu.idoluorder.infrastructure.out.persistence.r2dbc.repository;

import com.idolu.idoluorder.domain.payment.PaymentEvent;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface PaymentEventRepository extends R2dbcRepository<PaymentEvent, Long> {
}

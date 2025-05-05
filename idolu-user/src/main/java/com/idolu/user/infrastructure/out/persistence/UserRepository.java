package com.idolu.user.infrastructure.out.persistence;

import com.idolu.user.domain.user.User;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends R2dbcRepository<User, Long> {
    Mono<User> findByEmail(String email);
}

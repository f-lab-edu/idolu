package com.idolu.user.infrastructure.out.persistence;

import com.idolu.user.domain.user.Role;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface RoleRepository extends R2dbcRepository<Role, Long> {

    Mono<Role> findByName(String name);
}

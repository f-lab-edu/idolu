package com.idolu.user.infrastructure.out.adapter;

import com.idolu.user.domain.user.Role;
import com.idolu.user.global.exception.UserException;
import com.idolu.user.infrastructure.out.persistence.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import static com.idolu.user.global.exception.ResponseCode.ROLE_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class RoleAdapter {

    private final RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public Mono<Role> findRoleByName(String name) {
        return roleRepository.findByName(name)
                .switchIfEmpty(Mono.error(new UserException(ROLE_NOT_FOUND)));
    }
}

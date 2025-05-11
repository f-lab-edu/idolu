package com.idolu.user.infrastructure.out.adapter;

import com.idolu.user.domain.user.Role;
import com.idolu.user.infrastructure.out.persistence.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class RoleAdapter {

    private final RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public Mono<Role> findRoleByName(String name) {
        return roleRepository.findByName(name);
    }
}

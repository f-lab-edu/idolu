package com.idolu.user.application.user;

import com.idolu.user.domain.user.AuthenticatedUser;
import com.idolu.user.infrastructure.out.adapter.RoleAdapter;
import com.idolu.user.infrastructure.out.adapter.UserAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.function.TupleUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthUserDetailsService implements ReactiveUserDetailsService {

    private final UserAdapter userAdapter;
    private final RoleAdapter roleAdapter;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userAdapter.findUserByEmail(username)
                .zipWhen(user -> roleAdapter.findRoleById(user.getRoleId()))
                .map(TupleUtils.function((userEntity, role) -> new AuthenticatedUser(
                        userEntity.getUserId(),
                        userEntity.getUsername(),
                        userEntity.getPassword(),
                        userEntity.getEmail(),
                        userEntity.getPhone(),
                       List.of(new SimpleGrantedAuthority(role.getName())))));
    }
}

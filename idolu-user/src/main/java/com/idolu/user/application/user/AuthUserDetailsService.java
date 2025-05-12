package com.idolu.user.application.user;

import com.idolu.user.domain.user.AuthenticatedUser;
import com.idolu.user.infrastructure.out.adapter.UserAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthUserDetailsService implements ReactiveUserDetailsService {

    private final UserAdapter userAdapter;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userAdapter.findUserByEmail(username)
                .map(userEntity -> new AuthenticatedUser(
                        userEntity.getUserId(),
                        userEntity.getUsername(),
                        userEntity.getPassword(),
                        userEntity.getEmail(),
                        userEntity.getPhone(),
                        List.of()));
    }
}

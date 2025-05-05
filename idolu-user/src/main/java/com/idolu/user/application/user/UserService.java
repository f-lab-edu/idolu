package com.idolu.user.application.user;

import com.idolu.user.application.user.command.RegularUserSignUpCommand;
import com.idolu.user.domain.user.User;
import com.idolu.user.infrastructure.out.adapter.UserAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserAdapter userAdapter;
    private final EncryptService encryptService;

    public Mono<Long> signUp(RegularUserSignUpCommand command) {
        return userAdapter.validateUserNotExists(command.getEmail())
                .then(Mono.just(toUserEntity(command)))
                .flatMap(userAdapter::saveUser)
                .map(User::getUserId);
    }

    private User toUserEntity(RegularUserSignUpCommand command) {
        return User.builder()
                .email(command.getEmail())
                .password(encryptService.encrypt(command.getPassword()))
                .username(command.getUsername())
                .phone(command.getPhone())
                .role(command.getRole())
                .build();
    }
}

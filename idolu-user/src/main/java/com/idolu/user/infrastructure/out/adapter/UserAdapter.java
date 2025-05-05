package com.idolu.user.infrastructure.out.adapter;

import com.idolu.user.domain.user.User;
import com.idolu.user.global.exception.UserNotFoundException;
import com.idolu.user.infrastructure.out.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import static com.idolu.user.global.exception.ErrorCode.*;

@Component
@RequiredArgsConstructor
public class UserAdapter {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Mono<User> validateUserNotExists(String email) {
        return userRepository.findByEmail(email)
                .flatMap(user -> Mono.error(new UserNotFoundException(USER_ALREADY_EXIST)));
    }

    @Transactional(readOnly = true)
    public Mono<User> findUserByEmail(String email ) {
        return userRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new UserNotFoundException(USER_NOT_FOUND)));
    }

    @Transactional
    public Mono<User> saveUser(User userEntity) {
        return userRepository.save(userEntity);
    }
}

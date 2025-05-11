package com.idolu.user.application.user;

import com.idolu.user.application.user.command.RegularUserSignUpCommand;
import com.idolu.user.domain.user.Role;
import com.idolu.user.domain.user.User;
import com.idolu.user.infrastructure.out.adapter.RoleAdapter;
import com.idolu.user.infrastructure.out.adapter.UserAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.function.TupleUtils;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserAdapter userAdapter;
    private final RoleAdapter roleAdapter;
    private final EncryptService encryptService;

    public Mono<Long> signUp(RegularUserSignUpCommand command) {
        return Mono.zip(
                        userAdapter.validateUserNotExists(command.getEmail()),
                        roleAdapter.findRoleByName(command.getRole().name()))
                .map(TupleUtils.function((user, role) -> toUserEntity(command, role)))
                .flatMap(userAdapter::saveUser)
                .map(User::getUserId);
    }

    private User toUserEntity(RegularUserSignUpCommand command, Role role) {
        return User.builder()
                .roleId(role.getRoleId())
                .email(command.getEmail())
                .password(encryptService.encrypt(command.getPassword()))
                .username(command.getUsername())
                .phone(command.getPhone())
                .build();
    }
}

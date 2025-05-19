package com.idolu.user.global.authentication;

import com.idolu.user.global.utils.JwtTokenProvider;
import com.idolu.user.infrastructure.out.r2dbc.adapter.RoleAdapter;
import com.idolu.user.infrastructure.out.r2dbc.adapter.UserAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.function.TupleUtils;

import java.util.List;

import static com.idolu.user.global.exception.ResponseCode.INVALID_ACCESS_TOKEN;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserAdapter userAdapter;
    private final RoleAdapter roleAdapter;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.just(authentication)
                .filter(auth -> jwtTokenProvider.validateToken((String) auth.getCredentials()))
                .switchIfEmpty(Mono.error(new BadCredentialsException(INVALID_ACCESS_TOKEN.getMessage())))
                .map(auth -> (String) auth.getPrincipal())
                .flatMap(accessToken -> userAdapter.findUserBydId(jwtTokenProvider.getUserId(accessToken)))
                .zipWhen(user -> roleAdapter.findRoleById(user.getRoleId()))
                .map(TupleUtils.function((user, role) -> {
                    List<SimpleGrantedAuthority> simpleGrantedAuthorities = List.of(new SimpleGrantedAuthority(role.getName()));
                    return new UsernamePasswordAuthenticationToken(
                            new User(user.getUsername(), user.getPassword(), simpleGrantedAuthorities),
                            user.getUserId(),
                            simpleGrantedAuthorities);
                }));
    }
}

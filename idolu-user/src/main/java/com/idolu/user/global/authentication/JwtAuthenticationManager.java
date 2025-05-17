package com.idolu.user.global.authentication;

import com.idolu.user.application.user.TokenService;
import com.idolu.user.global.utils.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static com.idolu.user.global.exception.ResponseCode.INVALID_ACCESS_TOKEN;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtTokenProvider jwtTokenProvider;
    private final TokenService tokenService;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.just(authentication)
                .filter(auth -> jwtTokenProvider.validateToken((String) auth.getCredentials()))
                .switchIfEmpty(Mono.error(new BadCredentialsException(INVALID_ACCESS_TOKEN.getMessage())))
                .map(auth -> (String) auth.getPrincipal())
                .flatMap(tokenService::getAuthentication);
    }
}

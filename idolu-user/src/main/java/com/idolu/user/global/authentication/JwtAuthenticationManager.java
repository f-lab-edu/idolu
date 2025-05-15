package com.idolu.user.global.authentication;

import com.idolu.user.application.user.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtTokenProvider jwtTokenProvider;
    private final TokenService tokenService;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.just(authentication)
                .map(auth -> jwtTokenProvider.validateToken((String) auth.getCredentials()))
                .onErrorResume(e -> Mono.empty())
                .then(Mono.just((String) authentication.getPrincipal()))
                .flatMap(tokenService::getAuthentication);
    }
}

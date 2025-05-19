package com.idolu.user.global.authentication;

import com.idolu.user.global.utils.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtServerAuthenticationConverter implements ServerAuthenticationConverter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        return Mono.justOrEmpty(jwtTokenProvider.resolveAccessToken(exchange.getRequest()))
                .map(token -> new UsernamePasswordAuthenticationToken(token, token));
    }
}

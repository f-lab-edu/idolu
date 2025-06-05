package com.idolu.idoluorder.global.filter;

import com.idolu.idoluorder.global.common.OrderException;
import com.idolu.idoluorder.infrastructure.out.web.UserAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import static com.idolu.idoluorder.global.common.ResponseCode.INVALID_ACCESS_TOKEN;


@Component
@RequiredArgsConstructor
public class UserWebFilter implements WebFilter {

    private final UserAdapter userAdapter;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String authorization = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return Mono.error(new OrderException(INVALID_ACCESS_TOKEN));
        }

        return userAdapter.validateAccessToken(authorization)
                .flatMap(userId -> chain.filter(exchange)
                        .contextWrite(Context.of("userId", userId)));
    }
}

package com.idolu.idoluorder.infrastructure.web;

import com.idolu.idoluorder.global.common.ApiResponse;
import com.idolu.idoluorder.global.common.OrderException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


import static com.idolu.idoluorder.global.common.ResponseCode.INVALID_ACCESS_TOKEN;

@Component
@RequiredArgsConstructor
public class UserAdapter {

    private final WebClient userWebClient;

    public Mono<Long> validateAccessToken(String authorization) {
        return userWebClient.get()
                .uri("/api/v1/auth/validate")
                .header(HttpHeaders.AUTHORIZATION, authorization)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                    response.bodyToMono(String.class)
                            .flatMap(body -> Mono.error(new OrderException(INVALID_ACCESS_TOKEN)))
                )
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<Long>>() {})
                .map(ApiResponse::getData);
    }
}

package com.idolu.idoluorder.infrastructure.web;

import com.idolu.idoluorder.global.common.ApiResponse;
import com.idolu.idoluorder.global.common.OrderException;
import com.idolu.idoluorder.global.common.ResponseCode;
import com.idolu.idoluorder.infrastructure.web.response.ProductDetailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductAdapter {

    private final WebClient productWebClient;

    public Mono<ProductDetailResponse> getProductInformation(Long productId) {
        return productWebClient.get()
                .uri("/api/v1/products/" + productId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                    response.bodyToMono(String.class)
                            .flatMap(body -> Mono.error(new OrderException(ResponseCode.PRODUCT_NOT_FOUND))))
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<ProductDetailResponse>>() {})
                .map(ApiResponse::getData);
    }
}

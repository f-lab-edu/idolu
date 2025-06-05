package com.idolu.idoluorder.infrastructure.out.web;

import com.idolu.idoluorder.global.common.ProductRequestException;
import com.idolu.idoluorder.infrastructure.out.web.request.ProductStockUpdateRequest;
import com.idolu.idoluorder.infrastructure.out.web.response.ProductApiResponse;
import com.idolu.idoluorder.infrastructure.out.web.response.ProductDetailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
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
                .onStatus(HttpStatusCode::isError, this::createProductRequestException)
                .bodyToMono(new ParameterizedTypeReference<ProductApiResponse<ProductDetailResponse>>() {})
                .map(ProductApiResponse::getData);
    }

    public Mono<Boolean> decreaseProductStock(ProductStockUpdateRequest request) {
        return productWebClient.post()
                .uri("/api/v1/products/stocks")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .onStatus(statusCode -> statusCode.is4xxClientError() || statusCode.is5xxServerError(), this::createProductRequestException)
                .bodyToMono(new ParameterizedTypeReference<ProductApiResponse<Boolean>>() {})
                .map(ProductApiResponse::getData);
    }

    private Mono<ProductRequestException> createProductRequestException(ClientResponse clientResponse) {
        return clientResponse.bodyToMono(new ParameterizedTypeReference<ProductApiResponse<Void>>() {})
                .flatMap(data -> Mono.error(new ProductRequestException(data.getCode(), data.getMessage())));
    }
}

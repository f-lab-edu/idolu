package com.idolu.idoluorder.global.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

import javax.net.ssl.SSLException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.TimeUnit;


@Configuration
public class WebClientConfig {

    @Value("${external.user.url}")
    private String userServiceUrl;

    @Value("${external.product.url}")
    private String productServiceUrl;

    @Value("${external.toss.url}")
    private String tossServiceUrl;

    @Bean
    public WebClient userWebClient() throws SSLException {
        return createWebClient("userWebClient", userServiceUrl);
    }

    @Bean
    public WebClient productWebClient() throws SSLException {
        return createWebClient("productWebClient", productServiceUrl);
    }
    @Bean
    public WebClient tossWebClient() throws SSLException {
        return createWebClient("tossWebClient", tossServiceUrl);
    }


    private WebClient createWebClient(String name, String baseUrl) throws SSLException {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeaders(httpHeaders -> httpHeaders.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8)))
                .clientConnector(new ReactorClientHttpConnector(createHttpClient(name)))
                .build();
    }

    private HttpClient createHttpClient(String name) throws SSLException {
        HttpClient httpClient = HttpClient.create(
                ConnectionProvider.builder(name) // 커넥션 풀 이름
                        .maxConnections(200) // 최대 커넥션 수
                        .maxLifeTime(Duration.ofSeconds(30L)) // 연결 최대 시간
                        .maxIdleTime(Duration.ofSeconds(5L)) // 대기 상태 연결 최대 시간
                        .pendingAcquireMaxCount(1_000) // 커넥션풀 가득찰 때 대기 가능 요청 수
                        .pendingAcquireTimeout(Duration.ofSeconds(5L)) // 최대 대기 시간
                        .build())
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                .doOnConnected(conn -> {
                    conn.addHandlerLast(new ReadTimeoutHandler(3000L, TimeUnit.MILLISECONDS));
                    conn.addHandlerLast(new WriteTimeoutHandler(3000L, TimeUnit.MILLISECONDS));
                })
                .wiretap(HttpClient.class.getName(), LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL); // 연결 시도 타임아웃

        httpClient.warmup().block(); // HTTP 연결 풀을 미리 초기화하여 애플리케이션 시작 시 첫 요청 빠르게 수행되도록 보장
        return httpClient;
    }
}

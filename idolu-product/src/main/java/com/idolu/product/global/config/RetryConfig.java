package com.idolu.product.global.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.util.retry.Retry;

import java.time.Duration;

@Slf4j
@Configuration
public class RetryConfig {

    @Bean
    public Retry defaultBackoffRetry() {
        return Retry.backoff(3, Duration.ofSeconds(2)).jitter(0.1)
                .doBeforeRetry(retrySignal -> log.error("retryCount: {}, errorCode: {}", retrySignal.totalRetries(), retrySignal.failure().getMessage()))
                .onRetryExhaustedThrow((spec, retrySignal) -> retrySignal.failure());
    }
}

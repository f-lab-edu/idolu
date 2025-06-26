package com.idolu.product.infrastructure.in.kafka;

import com.idolu.product.application.product.ProductStockRollbackService;
import com.idolu.product.application.product.command.ProductStockRollbackCommand;
import com.idolu.product.global.utils.JsonConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Component;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockRollbackConsumer {

    private final JsonConverter jsonConverter;
    private final ProductStockRollbackService productStockRollbackService;

    @Bean
    public Disposable kafkaListener(ReactiveKafkaConsumerTemplate<String, String> consumerTemplate) {
        return consumerTemplate
                .receive()
                .groupBy(record -> record.receiverOffset().topicPartition()) // Partition 별로 groupBy
                .flatMap(partitions -> {
                    // Partition 별로 record 순차적으로 실행
                    return partitions.concatMap(record -> {
                        ProductStockRollbackCommand command = jsonConverter.toJson(record.value(), ProductStockRollbackCommand.class);

                        return productStockRollbackService.stockRollback(command)
                                .retryWhen(Retry.backoff(3, Duration.ofSeconds(2)).jitter(0.1)
                                        .doBeforeRetry(retrySignal -> log.error("retryCount: {}, errorCode: {}", retrySignal.totalRetries(), retrySignal.failure().getMessage()))
                                        .onRetryExhaustedThrow((spec, retrySignal) -> retrySignal.failure()))
                                .onErrorResume(error -> {
                                    log.error("consume stock rollback message: {}", error.getMessage());
                                    return Mono.just(true);
                                })
                                .doOnSuccess(result -> record.receiverOffset().acknowledge());
                    });
                })
                .subscribe();
    }
}

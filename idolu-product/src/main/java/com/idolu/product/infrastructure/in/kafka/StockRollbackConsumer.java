package com.idolu.product.infrastructure.in.kafka;

import com.idolu.product.application.product.ProductStockRollbackService;
import com.idolu.product.application.product.command.ProductStockRollbackCommand;
import com.idolu.product.global.utils.JsonConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Component;
import reactor.core.Disposable;
import reactor.util.retry.Retry;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockRollbackConsumer {

    @Value("${spring.kafka.consumer.topics.order-stock-rollback-dlt}")
    private String dltTopic;

    private final ReactiveKafkaProducerTemplate<String, String> kafkaProducer;
    private final JsonConverter jsonConverter;
    private final ProductStockRollbackService productStockRollbackService;
    private final Retry defaultBackoffRetry;

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
                                .retryWhen(defaultBackoffRetry)
                                .onErrorResume(error -> {
                                    log.error("consume stock rollback message error", error);
                                    return kafkaProducer.send(dltTopic, record.key(), record.value())
                                            .thenReturn(true);
                                })
                                .doOnSuccess(result -> record.receiverOffset().acknowledge()); // commit 처리
                    });
                })
                .subscribe();
    }
}

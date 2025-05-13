package com.idolu.user.infrastructure.out.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RedisAdapter {

    private final ReactiveRedisOperations<String, Object> redisOperations;
    private final ObjectMapper objectMapper;

    public Mono<String> getValue(String key) {
        return redisOperations.opsForValue().get(key).map(String::valueOf);
    }

    public Mono<Boolean> setValue(String key, Object value, Long expiredMs) {
        return redisOperations.opsForValue().set(key, value, Duration.ofSeconds(expiredMs));
    }

    public <T> Mono<T> getValueGeneric(String key, Class<T> clazz) {
        try {
            return redisOperations.opsForValue().get(key)
                    .switchIfEmpty(Mono.error(new RuntimeException("데이터가 없습니다: " + key)))
                    .flatMap(value -> Mono.just(objectMapper.convertValue(value, clazz)));
        } catch (Exception e) {
            e.getStackTrace();
            return Mono.error(new RuntimeException("error occured!" + e.getMessage()));
        }
    }
}

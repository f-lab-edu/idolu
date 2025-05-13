package com.idolu.user.application.user;

import com.idolu.user.infrastructure.out.redis.RedisAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TokenService {

    @Value("${jwt.expire.refresh-token}")
    private Long refreshTokenExpireHour;

    private final RedisAdapter redisAdapter;

    private static final String BASE_KEY = "auth:";

    public Mono<Boolean> upsertToken(String email, String refreshToken) {
        String key = BASE_KEY + email;

        return redisAdapter.setValue(key, refreshToken, refreshTokenExpireHour * 60 * 60);
    }
}

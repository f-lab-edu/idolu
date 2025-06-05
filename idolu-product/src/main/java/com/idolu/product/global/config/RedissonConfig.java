package com.idolu.product.global.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.password}")
    private String password;

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.database}")
    private int database;

    private static final String REDISSON_HOST_PREFIX = "redis://";

    @Bean
    public RedissonReactiveClient redissonReactiveClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress(REDISSON_HOST_PREFIX + host + ":" + port)
                .setPassword(password)
                .setDatabase(database)
                .setKeepAlive(true)
                .setClientName("product-service");

        return Redisson.create(config).reactive();
    }
}

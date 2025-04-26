package com.example.springredissoncodecbycache.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

import static com.example.springredissoncodecbycache.config.RedisCacheManagerConfig.TEST_REDISSON_PROTOBUF_CACHE_NAME;

@EnableCaching
@Configuration
public class RedissonCacheManagerConfig {

    @Bean
    public RedissonClient redissonClient() {
        final var config = new Config();
        config.setCodec(new UserProtoRedisCodec()).useSingleServer()
                .setAddress("redis://localhost:6380")
                .setDatabase(0);
        return Redisson.create(config);
    }

    @Bean
    public CacheManager redissonCacheManager(
            RedissonClient redissonClient
    ) {
        final var cacheConfig = new CacheConfig();
        cacheConfig.setMaxIdleTime(1_000_000);
        cacheConfig.setTTL(1_000_000);
        cacheConfig.setMaxSize(1_000_000);
        return new RedissonSpringCacheManager(redissonClient, Map.of(
                TEST_REDISSON_PROTOBUF_CACHE_NAME, cacheConfig
        ));
    }
}

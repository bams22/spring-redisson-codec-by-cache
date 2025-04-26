package com.example.springredissoncodecbycache.service;

import com.example.springredissoncodecbycache.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.springredissoncodecbycache.config.RedisCacheManagerConfig.TEST_REDISSON_PROTOBUF_CACHE_NAME;

@RequiredArgsConstructor
@Service
public class UserCacheService {

    private final ReactiveRedisTemplate<String, User> redisTemplate;

    @Cacheable(key = "#key", cacheManager = "redissonCacheManager", cacheNames = TEST_REDISSON_PROTOBUF_CACHE_NAME)
    public User toCacheSync(User user, String key) {
        return user;
    }

    @Cacheable(key = "#key", cacheManager = "redissonCacheManager", cacheNames = TEST_REDISSON_PROTOBUF_CACHE_NAME)
    public Optional<User> toCacheSync2(User user, String key) {
        return Optional.ofNullable(user);
    }
}

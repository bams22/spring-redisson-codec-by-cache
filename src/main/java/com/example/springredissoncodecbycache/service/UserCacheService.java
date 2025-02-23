package com.example.springredissoncodecbycache.service;

import com.example.springredissoncodecbycache.entity.User;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;

import static com.example.springredissoncodecbycache.config.RedisCacheManagerConfig.TEST_KRYO_CACHE_NAME;

@RequiredArgsConstructor
@Service
public class UserCacheService {

    private final ReactiveRedisTemplate<String, User> redisTemplate;

    @Cacheable(value = TEST_KRYO_CACHE_NAME, key = "#user.id")
    @Nonnull
    public User toCache(@Nonnull User user) {
        return user;
    }
}

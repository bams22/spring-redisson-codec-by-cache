package com.example.springredissoncodecbycache.service;

import com.example.springredissoncodecbycache.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
@Service
public class UserReactiveCacheService {

    private final ReactiveRedisTemplate<String, User> redisTemplate;

    public Mono<User> toCache(User user) {
        return getData(user.getId().toString(), user);
    }

    // Метод получения данных с кэшированием
    private Mono<User> getData(String key, User user) {
        // Пытаемся получить данные из Redis
        return redisTemplate.opsForValue().get(key)
                // Если данных в кэше нет, выполняем запрос к источнику данных
                .switchIfEmpty(
                        Mono.just(user)
                                // После получения данных сохраняем их в Redis и возвращаем результат
                                .flatMap(data -> redisTemplate.opsForValue()
                                        .set(key, data)
                                        .thenReturn(data))
                );
    }
}

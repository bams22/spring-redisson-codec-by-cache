package com.example.springredissoncodecbycache.controller;

import com.example.springredissoncodecbycache.service.UserCacheService;
import com.example.springredissoncodecbycache.service.UserReactiveCacheService;
import com.example.springredissoncodecbycache.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.Instant;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/cache/user")
public class UserCacheController {

    private final UserCacheService userCacheService;
    private final UserReactiveCacheService userReactiveCacheService;
    private final UserService userService;

    @PostMapping("/sync")
    public void generateUserSync() {
        final var startTime = Instant.now();
        final var list = userService.findAllReactive()
                .limitRate(1_000)
                .take(1_000_000)
                .collectList()
                .block();
        Assert.notNull(list, "List is null");
        list.forEach(userCacheService::toCache);
        final var endTime = Instant.now();
        Duration duration = Duration.between(startTime, endTime);
        log.info("Время выполнения: {} (seconds)", duration.getSeconds());

    }

    @PostMapping("/async")
    public void generateUserAsync() {
        final var startTime = Instant.now();
        userService.findAllReactive()
                .limitRate(1_000)
                .take(1_000_000)
                .subscribeOn(Schedulers.single())
                .flatMap(userReactiveCacheService::toCache)
                .blockLast();
        final var endTime = Instant.now();
        Duration duration = Duration.between(startTime, endTime);
        log.info("Время выполнения: {} (seconds)", duration.getSeconds());
    }
}

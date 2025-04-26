package com.example.springredissoncodecbycache.service;

import com.example.springredissoncodecbycache.generator.UserGenerator;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TestService {

    private final UserGenerator userGenerator;

    private final UserCacheService userCacheService;

//    @PostConstruct
//    public void test() {
//        final var userUuid = UUID.randomUUID();
//        userCacheService.toCacheSync(null, userUuid.toString());
//        userCacheService.toCacheSync(null, userUuid.toString());
//    }

    public void test() {
        final var user = userGenerator.generateUser();
        userCacheService.toCacheSync2(null, user.getId().toString());
        userCacheService.toCacheSync2(null, user.getId().toString());
    }
}

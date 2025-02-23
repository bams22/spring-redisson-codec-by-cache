package com.example.springredissoncodecbycache.controller;

import com.example.springredissoncodecbycache.entity.User;
import com.example.springredissoncodecbycache.generator.UserGenerator;
import com.example.springredissoncodecbycache.repository.r2dbc.UserReactiveRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/generate/user")
public class UserGenerationController {

    private final UserGenerator userGenerator;
    private final UserReactiveRepository userRepository;

    @PostMapping
    public void generateUser() {
        Flux.range(0, 10000)
                .map(ignored -> generateUsers(1000))
                .flatMap(userRepository::saveAll)
                .blockLast();
    }

    private List<User> generateUsers(int count) {
        return IntStream.range(0, count)
                .mapToObj(ignored -> userGenerator.generateUser())
                .toList();
    }
}

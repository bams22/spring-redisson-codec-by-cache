package com.example.springredissoncodecbycache.service;

import com.example.springredissoncodecbycache.entity.User;
import com.example.springredissoncodecbycache.repository.r2dbc.UserReactiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserReactiveRepository userReactiveRepository;

    public Flux<User> findAllReactive() {
        return userReactiveRepository.findAll(
                Sort.by("id")
        );
    }
}

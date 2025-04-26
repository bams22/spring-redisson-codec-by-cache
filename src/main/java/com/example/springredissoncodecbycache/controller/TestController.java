package com.example.springredissoncodecbycache.controller;

import com.example.springredissoncodecbycache.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/test")
public class TestController {

    private final TestService testService;

    @GetMapping
    public void test() {
        testService.test();
    }
}

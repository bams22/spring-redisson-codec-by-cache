package com.example.springredissoncodecbycache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@EnableR2dbcRepositories(basePackages = "com.example.springredissoncodecbycache.repository.r2dbc")
@SpringBootApplication
public class SpringRedissonCodecByCacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringRedissonCodecByCacheApplication.class, args);
    }

}

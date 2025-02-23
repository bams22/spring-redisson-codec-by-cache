package com.example.springredissoncodecbycache.generator;

import com.example.springredissoncodecbycache.entity.User;
import com.github.javafaker.Faker;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Component
public class UserGenerator {

    private static final Faker faker = new Faker();

    public User generateUser() {
        Date pastDate = faker.date().past(365 * 10, java.util.concurrent.TimeUnit.DAYS); // Random date in the past 10 years
        Instant dateCreated = pastDate.toInstant();
        Instant dateUpdated = faker.date().between(pastDate, new Date()).toInstant(); // Random date between dateCreated and now

        return User.builder()
                .id(UUID.randomUUID())
                .name(faker.name().fullName())
                .email(faker.internet().emailAddress())
                .password(faker.internet().password())
                .isActive(faker.bool().bool())
                .cityUuid(UUID.randomUUID())
                .dateCreated(dateCreated)
                .dateUpdated(dateUpdated)
                .isNew(true)
                .build();
    }
}

package com.example.springredissoncodecbycache.repository.r2dbc;

import com.example.springredissoncodecbycache.entity.User;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import java.util.UUID;

public interface UserReactiveRepository extends R2dbcRepository<User, UUID> {
}

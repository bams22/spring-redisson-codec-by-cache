package com.example.springredissoncodecbycache.config;

import com.example.springredissoncodecbycache.entity.User;
import com.example.springredissoncodecbycache.proto.UserOuterClass;
import com.google.protobuf.Timestamp;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.time.Instant;
import java.util.UUID;

public class ProtobufRedisSerializer implements RedisSerializer<User> {

    @Override
    public byte[] serialize(User user) throws SerializationException {
        return serializeInternal(map(user));
    }

    @Override
    public User deserialize(byte[] bytes) throws SerializationException {
        return map(deserializeInternal(bytes));
    }

    public byte[] serializeInternal(UserOuterClass.User user) throws SerializationException {
        if (user == null) {
            return new byte[0];
        }
        return user.toByteArray();
    }

    public UserOuterClass.User deserializeInternal(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try {
            return UserOuterClass.User.parseFrom(bytes);
        } catch (Exception e) {
            throw new SerializationException("Ошибка десериализации Protobuf", e);
        }
    }

    private User map(UserOuterClass.User user) {
        return User.builder()
                .id(new UUID(user.getIdMostSigBits(), user.getIdLeastSigBits()))
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .isActive(user.getIsActive())
                .cityUuid(new UUID(user.getCityUuidMostSigBits(), user.getCityUuidLeastSigBits()))
                .dateCreated(timestampToInstant(user.getDateCreated()))
                .dateUpdated(timestampToInstant(user.getDateUpdated()))
                .build();
    }

    private UserOuterClass.User map(User user) {
        return UserOuterClass.User.newBuilder()
                .setIdMostSigBits(user.getId().getMostSignificantBits())
                .setIdLeastSigBits(user.getId().getLeastSignificantBits())
                .setName(user.getName())
                .setEmail(user.getEmail())
                .setPassword(user.getPassword())
                .setIsActive(user.getIsActive())
                .setCityUuidMostSigBits(user.getCityUuid().getMostSignificantBits())
                .setCityUuidLeastSigBits(user.getCityUuid().getLeastSignificantBits())
                .setDateCreated(instantToTimestamp(user.getDateCreated()))
                .setDateUpdated(instantToTimestamp(user.getDateUpdated()))
                .build();
    }

    public static Instant timestampToInstant(Timestamp timestamp) {
        return Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos());
    }

    public static Timestamp instantToTimestamp(Instant instant) {
        return Timestamp.newBuilder()
                .setSeconds(instant.getEpochSecond())
                .setNanos(instant.getNano())
                .build();
    }
}

package com.example.springredissoncodecbycache.config;

import com.example.springredissoncodecbycache.entity.User;
import com.example.springredissoncodecbycache.proto.UserOuterClass;
import com.google.protobuf.Timestamp;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.redisson.client.codec.BaseCodec;
import org.redisson.client.codec.StringCodec;
import org.redisson.client.protocol.Decoder;
import org.redisson.client.protocol.Encoder;
import org.redisson.spring.cache.NullValue;
import org.springframework.data.redis.serializer.SerializationException;

import java.time.Instant;
import java.util.UUID;

public class UserProtoRedisCodec extends BaseCodec {

    private final StringCodec stringCodec = new StringCodec();

    @Override
    public Decoder<Object> getMapKeyDecoder() {
        return stringCodec.getMapKeyDecoder();
    }

    @Override
    public Encoder getMapKeyEncoder() {
        return stringCodec.getMapKeyEncoder();
    }

    @Override
    public Decoder<Object> getValueDecoder() {
        return decoder;
    }

    @Override
    public Encoder getValueEncoder() {
        return encoder;
    }

    private final Decoder<Object> decoder = (buf, state) -> {
        try {
            byte[] bytes = new byte[buf.readableBytes()];
            buf.getBytes(buf.readerIndex(), bytes);
            if (bytes.length == 0 || (bytes.length == 1 && bytes[0] == 0)) {
                return NullValue.INSTANCE;
            }
            return map(UserOuterClass.User.parseFrom(bytes));
        } catch (Exception e) {
            throw new SerializationException("Ошибка при десериализации объекта", e);
        }
    };

    private final Encoder encoder = in -> {
        ByteBuf out = ByteBufAllocator.DEFAULT.buffer();
        try {
            if (in == null || in == NullValue.INSTANCE) {
                out.writeBytes(new byte[] {0});
            } else {
                out.writeBytes(map((User) in).toByteArray());
            }
            return out;
        } catch (Exception e) {
            throw new SerializationException("Ошибка при сериализации объекта", e);
        }
    };

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

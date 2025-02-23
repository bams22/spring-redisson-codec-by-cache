package com.example.springredissoncodecbycache.config;

import com.esotericsoftware.kryo.kryo5.Kryo;
import com.esotericsoftware.kryo.kryo5.io.Input;
import com.esotericsoftware.kryo.kryo5.io.Output;
import com.esotericsoftware.kryo.kryo5.serializers.DefaultSerializers;
import com.example.springredissoncodecbycache.entity.User;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.Instant;
import java.util.UUID;

public class KryoRedisSerializer<T> implements RedisSerializer<T> {

    private final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.register(User.class); // Регистрация класса
        kryo.register(UUID.class, new DefaultSerializers.UUIDSerializer()); // Регистрация класса
        kryo.register(Instant.class, new InstantKryoSerializer()); // Регистрация класса
        return kryo;
    });
    private final Class<T> type;

    public KryoRedisSerializer(Class<T> type) {
        this.type = type;
    }

    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (t == null) {
            return new byte[0];
        }
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             Output output = new Output(baos)) {
            kryoThreadLocal.get().writeObject(output, t);
            output.flush();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new SerializationException("Ошибка при сериализации объекта", e);
        }
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
             Input input = new Input(bais)) {
            return kryoThreadLocal.get().readObject(input, type);
        } catch (Exception e) {
            throw new SerializationException("Ошибка при десериализации объекта", e);
        }
    }
}

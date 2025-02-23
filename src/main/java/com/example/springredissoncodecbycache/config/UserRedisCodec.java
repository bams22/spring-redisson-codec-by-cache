package com.example.springredissoncodecbycache.config;
import com.esotericsoftware.kryo.kryo5.Kryo;
import com.esotericsoftware.kryo.kryo5.io.Input;
import com.esotericsoftware.kryo.kryo5.io.Output;
import com.esotericsoftware.kryo.kryo5.serializers.DefaultSerializers;
import com.example.springredissoncodecbycache.entity.User;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufOutputStream;
import org.redisson.client.codec.BaseCodec;
import org.redisson.client.codec.StringCodec;
import org.redisson.client.protocol.Decoder;
import org.redisson.client.protocol.Encoder;
import org.springframework.data.redis.serializer.SerializationException;

import java.io.ByteArrayInputStream;
import java.time.Instant;
import java.util.UUID;

public class UserRedisCodec extends BaseCodec {

    private final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.register(User.class);
        kryo.register(UUID.class, new DefaultSerializers.UUIDSerializer());
        kryo.register(Instant.class, new InstantKryoSerializer());
        return kryo;
    });
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
        try (ByteArrayInputStream bais = new ByteArrayInputStream(buf.array());
             Input input = new Input(bais)) {
            return kryoThreadLocal.get().readObject(input, User.class);
        } catch (Exception e) {
            throw new SerializationException("Ошибка при десериализации объекта", e);
        }
    };

    private final Encoder encoder = in -> {
        ByteBuf out = ByteBufAllocator.DEFAULT.buffer();
        try (final var baos = new ByteBufOutputStream(out);
             final var output = new Output(baos)) {
            kryoThreadLocal.get().writeObject(output, in);
            output.flush();
            return baos.buffer();
        } catch (Exception e) {
            throw new SerializationException("Ошибка при сериализации объекта", e);
        }
    };
}

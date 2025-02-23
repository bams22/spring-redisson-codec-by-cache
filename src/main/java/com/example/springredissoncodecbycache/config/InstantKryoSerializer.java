package com.example.springredissoncodecbycache.config;

import com.esotericsoftware.kryo.kryo5.Kryo;
import com.esotericsoftware.kryo.kryo5.Serializer;
import com.esotericsoftware.kryo.kryo5.io.Input;
import com.esotericsoftware.kryo.kryo5.io.Output;

import java.time.Instant;

public class InstantKryoSerializer extends Serializer<Instant> {

    @Override
    public void write(Kryo kryo, Output output, Instant instant) {
        output.writeLong(instant.getEpochSecond());
        output.writeInt(instant.getNano());
    }

    @Override
    public Instant read(Kryo kryo, Input input, Class<? extends Instant> type) {
        long seconds = input.readLong();
        int nanos = input.readInt();
        return Instant.ofEpochSecond(seconds, nanos);
    }
}

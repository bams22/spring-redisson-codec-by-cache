package com.example.springredissoncodecbycache.config;

import com.example.springredissoncodecbycache.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.*;

import java.time.Duration;
import java.time.Instant;

@EnableCaching
@Configuration
public class RedisCacheManagerConfig {

    public static final String TEST_JDK_CACHE_NAME = "user_jdk";
    public static final String TEST_KRYO_CACHE_NAME = "user_kryo";
    public static final String TEST_JSON_CACHE_NAME = "user_json";
    public static final String TEST_PROTOBUF_CACHE_NAME = "user_protobuf";
    public static final String TEST_REDISSON_PROTOBUF_CACHE_NAME = "user_redisson_protobuf";

    @Primary
    @Bean
    public CacheManager redisCacheManager(
            RedisConnectionFactory redisConnectionFactory,
            ObjectProvider<RedisCacheManagerBuilderCustomizer> customizers
    ) {

        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(cacheConfiguration())
                .transactionAware();

        customizers.orderedStream().forEach(customizer -> customizer.customize(builder));

        return builder.build();
    }

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(10))
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(
                                new StringRedisSerializer()
                        )
                ).serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(
                                new JdkSerializationRedisSerializer()
                        )
                );
    }

    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        final var objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(Instant.class, InstantSerializer.INSTANCE);
        javaTimeModule.addDeserializer(Instant.class, InstantDeserializer.INSTANT);
        objectMapper.registerModule(javaTimeModule);
        objectMapper.findAndRegisterModules();

        return (builder) -> builder
                .withCacheConfiguration(TEST_JDK_CACHE_NAME,
                        RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(10))
                                .serializeValuesWith(
                                        RedisSerializationContext.SerializationPair.fromSerializer(
                                                new JdkSerializationRedisSerializer()
                                        )
                                )
                ).withCacheConfiguration(TEST_KRYO_CACHE_NAME,
                        RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(10))
                                .serializeValuesWith(
                                        RedisSerializationContext.SerializationPair.fromSerializer(
                                                new KryoRedisSerializer<>(User.class)
                                        )
                                )
                ).withCacheConfiguration(TEST_JSON_CACHE_NAME,
                        RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(10))
                                .serializeValuesWith(
                                        RedisSerializationContext.SerializationPair.fromSerializer(
                                                new Jackson2JsonRedisSerializer<>(objectMapper, User.class)
                                        )
                                )
                ).withCacheConfiguration(TEST_PROTOBUF_CACHE_NAME,
                        RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(10))
                                .serializeValuesWith(
                                        RedisSerializationContext.SerializationPair.fromSerializer(
                                                new ProtobufRedisSerializer()
                                        )
                                )
                );
    }
}

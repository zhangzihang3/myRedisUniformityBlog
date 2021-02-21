package com.zzh.data.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
public class redisConfig {
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    /**
     * 自定义redisCacheManager
     * @return RedisCacheManager
     */
    @Bean(value = "redisCacheManager")
    public RedisCacheManager redisCacheManager() {
        RedisCacheConfiguration conf = RedisCacheConfiguration.defaultCacheConfig();
        conf = conf.entryTtl(Duration.ofSeconds(300000));
        RedisCacheManager cacheManager = RedisCacheManager
                .builder(redisConnectionFactory)
                .cacheDefaults(conf)
                .build();
        return cacheManager;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        //TODO:指定Key、Value的序列化策略
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        return redisTemplate;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate() {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(redisConnectionFactory);
        return stringRedisTemplate;
    }

}

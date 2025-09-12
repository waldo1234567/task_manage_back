package com.task_management.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class RedisConfig {
    @Bean
    public LettuceConnectionFactory redisConnectionFactory(){
        return new LettuceConnectionFactory();
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(LettuceConnectionFactory factory){
        StringRedisTemplate t = new StringRedisTemplate(factory);
        return t;
    }

}

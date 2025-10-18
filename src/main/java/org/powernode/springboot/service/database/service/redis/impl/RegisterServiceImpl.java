package org.powernode.springboot.service.database.service.redis.impl;

import org.powernode.springboot.service.database.service.redis.RegisterService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RegisterServiceImpl implements RegisterService {
    private final RedisTemplate<String, Object> redisTemplate;

    RegisterServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    @Override
    public void setVerifyCode(String email, String token) {
        redisTemplate.opsForValue().set(email,token, Duration.ofMinutes(3));
    }

    @Override
    public boolean hasVerifyCode(String email) {
        return redisTemplate.hasKey(email);
    }

    @Override
    public boolean checkVerifyCode(String email, String token) {
        String value=(String) redisTemplate.opsForValue().get(email);
        return token.equals(value);
    }
}

package org.powernode.springboot.service.database.service.redis.impl;

import org.powernode.springboot.bean.database.LoginToken;
import org.powernode.springboot.service.database.service.redis.LoginTokenService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
public class LoginTokenServiceImpl implements LoginTokenService {
    private final RedisTemplate<String, Object> redisTemplate;

    LoginTokenServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void setTokenStartValidTime(String key, long time) {
        redisTemplate.opsForValue().set(key, time);
    }

    @Override
    public long getTokenStartValidTime(String key) {
        Long time= (Long)redisTemplate.opsForValue().get(key);
        if(time==null)
            time=-1L;
        return time;
    }
}

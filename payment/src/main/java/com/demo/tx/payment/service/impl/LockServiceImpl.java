package com.demo.tx.payment.service.impl;

import com.demo.tx.payment.service.LockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.consul.serviceregistry.ConsulAutoRegistration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class LockServiceImpl implements LockService {
    private final String LOCK_KEY_PREFIX = "LOCK_";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private LocalRedisScriptContainer redisScriptContainer;

    @Autowired
    private ConsulAutoRegistration consulAutoRegistration;

    @Override
    public boolean lock(String key) {
        return lock(key, 10000);
    }

    @Override
    public boolean lock(String key, long expireMilliSecs) {
        final RedisScript<String> redisScript = redisScriptContainer.getRedisScript("lock.lua");

        final String result = stringRedisTemplate.execute(redisScript, Collections.singletonList(LOCK_KEY_PREFIX + key),
                getLockOwner(), String.valueOf(expireMilliSecs));

        return result != null && Integer.parseInt(result) > 0;
    }

    @Override
    public boolean unlock(String key) {
        final RedisScript<String> delByValueScript = redisScriptContainer.getRedisScript("unlock.lua");
        final String result = stringRedisTemplate.execute(delByValueScript, Collections.singletonList(LOCK_KEY_PREFIX + key),
                getLockOwner());

        return result != null && Integer.parseInt(result) > 0;
    }

    private String getLockOwner() {
        return consulAutoRegistration.getInstanceId() + ":" + Thread.currentThread().getName()+":";
    }

}

package com.demo.tx.payment.service.impl;

import com.demo.tx.payment.service.IdGenerator;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class IdGeneratorImpl implements IdGenerator {
    private final String ID_KEY_PREFIX = "ID_";
    private LoadingCache<String, RedisAtomicLong> cache;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public long nextId(String key) {
        try {
            final RedisAtomicLong idGenerator = cache.get(ID_KEY_PREFIX + key);
            return idGenerator.incrementAndGet();
        } catch (ExecutionException e) {
            log.error("Error occur when getting redis id key: {}", ID_KEY_PREFIX + key, e);
            throw new RuntimeException(e);
        }
    }

    @PostConstruct
    public void initCache() {
        cache = CacheBuilder.newBuilder().expireAfterAccess(1000, TimeUnit.SECONDS)
                .maximumSize(1000)
                .concurrencyLevel(10)
                .weakKeys()
                .weakValues()
                .refreshAfterWrite(2L, TimeUnit.SECONDS)
                .build(new CacheLoader<String, RedisAtomicLong>() {
                    @Override
                    public RedisAtomicLong load(String key) throws Exception {
                        // should get from db or other persist resource, if not present
                        return new RedisAtomicLong(ID_KEY_PREFIX + key, stringRedisTemplate.getConnectionFactory());
                    }
                });
    }

}

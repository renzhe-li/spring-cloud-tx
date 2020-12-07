package com.demo.tx.payment.service.impl;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LocalRedisScriptContainer implements RedisScriptContainer{
    private Map<String, DefaultRedisScript<String>> redisScriptMap = new ConcurrentHashMap<>();

    @Override
    public RedisScript<String> getRedisScript(String luaFilename) {
        DefaultRedisScript<String> redisScript = redisScriptMap.get(luaFilename);
        if (redisScript == null) {
            redisScript = new DefaultRedisScript<>();

            redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/" + luaFilename)));
            redisScript.setResultType(String.class);

            redisScriptMap.put(luaFilename, redisScript);
        }

        return redisScript;
    }
}
